package commands

import info.Info
import node.Node
import node.visitors.*
import node.visitors.modifiers.Composed
import node.visitors.modifiers.Flagged
import node.visitors.modifiers.PreOrder
import java.util.function.Predicate

class Builder
    (
    private val nodename: String,
    private val env: String,
    private val skipTests: Boolean,
    private val children: Boolean,
    private val force: Boolean,
    val info: Info
) {
    fun build() {
        val mavenBuilder = MavenBuilder(info, skipTests, env)
        val projectFilter =
            if (children) Predicate { it.dependsOn(nodename) } else Predicate<Node> { it.name == nodename }
        info.projects.all().filter { it.isDownloaded(info.workspace) }
            .filter { projectFilter.test(it) }.parallelStream()
            .forEach { node ->
                node.acceptVisitor(
                    PreOrder(
                        Composed(
                            RepoStatusPrinter(info),
                            Updater(info),
                            LocalChangeChecker(info, env),
                            if (!force) Flagged("dirty", mavenBuilder) else mavenBuilder,
                            LocalCacheUpdater(info, env)
                        )
                    )
                )
            }
    }
}