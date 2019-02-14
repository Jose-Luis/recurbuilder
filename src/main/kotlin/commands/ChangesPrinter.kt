package commands

import info.Info
import node.Node
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
        val filter = if (children) Predicate { it.dependsOn(nodename) } else Predicate<Node> { it.name == nodename }
        info.projects.all().filter { filter.test(it) }.parallelStream().forEach { node ->
            node.acceptVisitor(PreOrder(Composed(RepoStatusPrinter(info))))
        }
    }
}