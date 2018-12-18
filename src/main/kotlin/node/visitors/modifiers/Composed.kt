package node.visitors.modifiers

import node.Node
import node.visitors.NodeVisitor

class Composed(vararg val visitors: NodeVisitor) : NodeVisitor {
    override fun visit(node: Node) {
        visitors.forEach { it.visit(node) }
    }
}