package commands

import info.Info
import node.Node
import node.visitors.*
import node.visitors.modifiers.Composed
import node.visitors.modifiers.Flagged
import node.visitors.modifiers.PreOrder
import java.io.File
import java.util.function.Predicate

class BranchExecutor
    (
    private val nodename: String,
    val children: Boolean,
    val command: String,
    val info: Info
) {
    fun execute() {
        val projectFilter =
            if (children) Predicate { it.dependsOn(nodename) } else Predicate<Node> { it.name == nodename }
        info.projects.all().filter { projectFilter.test(it) }.parallelStream().forEach { node ->
            node.acceptVisitor(
                PreOrder(Executor(command))
            )
        }
    }
}