package commands

import info.Info
import node.Node
import node.visitors.*
import node.visitors.modifiers.Composed
import node.visitors.modifiers.Flagged
import node.visitors.modifiers.PreOrder
import java.io.File
import java.util.function.Predicate

class Builder
    (
    val nodename: String,
    val env: String,
    val noTests: Boolean,
    val children: Boolean,
    val force: Boolean,
    val infoFile: File
) {

    fun build(): Unit {
        val info = Info(infoFile)
        val buildCommand = info.commands.build.plus(if (noTests) " -DskipTests" else "").plus(" -Denv=$env")
        val projectFilter =
            if (children) Predicate { it.dependsOn(nodename) } else Predicate<Node> { it.name == nodename }
        info.projects.all().filter { projectFilter.test(it) }.parallelStream().forEach { node ->
            node.acceptVisitor(
                PreOrder(
                    Composed(
                        Printer(info.commands.print),
                        Updater(info.commands.update),
                        ChangeChecker(info.cache, env),
                        if (!force) Flagged("dirty", MavenExecuter(buildCommand)) else MavenExecuter(buildCommand),
                        CacheUpdater(info.cache, env)
                    )
                )
            )
        }
    }
}