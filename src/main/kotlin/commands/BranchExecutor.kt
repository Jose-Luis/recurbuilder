package commands

import info.Info
import node.Node.Companion.dependsOn
import node.Node.Companion.only
import node.visitors.Executor
import node.visitors.Printer
import node.visitors.modifiers.PreOrder

class BranchExecutor
    (
    private val nodename: String,
    val children: Boolean,
    val command: String,
    val info: Info
) {
    fun execute() = info.projects.visit(if (children) dependsOn(nodename) else only(nodename))
        .with(PreOrder(Printer(), Executor(info, command)))
}