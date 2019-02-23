package commands

import info.Info
import node.Node.Companion.dependsOn
import node.Node.Companion.only
import node.visitors.*
import node.visitors.modifiers.Composed
import node.visitors.modifiers.Downloaded
import node.visitors.modifiers.OnlyIf
import node.visitors.modifiers.PreOrder

class Builder
    (
    private val nodename: String,
    private val env: String,
    private val skipTests: Boolean,
    private val force: Boolean,
    private val mode: String,
    val info: Info
) {
    private val builder = Downloaded(
        info,
        Composed(
            Printer(),
            RepoStatusPrinter(info),
            Updater(info),
            LocalChangeChecker(info, env),
            if (force) MavenBuilder(info, skipTests, env) else OnlyIf("dirty", MavenBuilder(info, skipTests, env)),
            LocalCacheUpdater(info, env)
        )
    )

    fun build() {
        when (mode) {
            "deps" -> info.projects.visit(only(nodename)).with(PreOrder(builder))
            "cascade" -> info.projects.visit(dependsOn(nodename)).with(PreOrder(builder))
            "alone" -> info.projects.visit(only(nodename)).with(builder)
            else -> throw NotImplementedError()
        }
    }
}