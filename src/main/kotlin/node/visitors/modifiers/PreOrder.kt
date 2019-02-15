package node.visitors.modifiers

import node.Node
import node.visitors.NodeVisitor

class PreOrder(vararg visitors: NodeVisitor) : NodeVisitor {
    private val once = Once(Composed(*visitors))
    override fun visit(node: Node) {
        node.deps.parallelStream().forEach { it.acceptVisitor(this) }
        once.visit(node)
    }
}