package commands

import info.Info
import node.Node
import node.visitors.*
import node.visitors.modifiers.Composed
import node.visitors.modifiers.Once
import node.visitors.modifiers.PreOrder
import java.util.function.Predicate

class BranchExecutor
    (
    private val nodename: String,
    val children: Boolean,
    val command: String,
    val info: Info
) {
    fun execute() {
        info.projects.visit(
            if (children) { node -> node.dependsOn(nodename) } else { node -> node.name == nodename },
            PreOrder(Printer(), Executor(info, command))
        )
    }

}