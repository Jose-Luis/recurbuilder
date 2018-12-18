package node.visitors.modifiers

import node.Node
import node.visitors.NodeVisitor

class PreOrder(val visitor: NodeVisitor) : NodeVisitor {
    override fun visit(node: Node) {
        node.deps.parallelStream().forEach { it.acceptVisitor(this) }
        visitor.visit(node)
    }
}