package commands

import info.Info
import node.visitors.Executor
import node.visitors.modifiers.Downloaded

class BranchExecutor
    (
    val command: String,
    val info: Info
) {
    fun execute() = info.projects.all().forEach { it.acceptVisitor(Downloaded(info, Executor(info, command))) }
}