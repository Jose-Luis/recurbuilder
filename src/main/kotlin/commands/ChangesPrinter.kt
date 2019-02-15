package commands

import info.Info
import node.Node
import node.Node.Companion.dependsOn
import node.Node.Companion.only
import node.visitors.Printer
import node.visitors.RepoStatusPrinter
import node.visitors.modifiers.PreOrder

class ChangesPrinter
    (
    private val nodename: String,
    private val children: Boolean,
    val info: Info
) {
    fun printChanges() = info.projects.visit(if (children) dependsOn(nodename) else only(nodename))
        .with(PreOrder(Printer(), RepoStatusPrinter(info)))
}