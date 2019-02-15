package commands

import info.Info
import node.Node
import node.visitors.Printer
import node.visitors.RepoStatusPrinter
import node.visitors.modifiers.Composed
import node.visitors.modifiers.PreOrder
import java.util.function.Predicate

class ChangesPrinter
    (
    private val nodename: String,
    private val children: Boolean,
    val info: Info
) {
    fun printChanges() {
        info.projects.visit(
            if (children) { node -> node.dependsOn(nodename) } else { node -> node.name == nodename },
            PreOrder(Printer(),RepoStatusPrinter(info))
        )
    }
}