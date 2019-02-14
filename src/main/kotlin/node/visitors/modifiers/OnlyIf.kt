package node.visitors.modifiers

import node.Node
import node.visitors.NodeVisitor

class OnlyIf(val flag:String, val visitor: NodeVisitor) : NodeVisitor {
    override fun visit(node: Node) {
        if (node.hasOrInheritFlag(flag)) {
            visitor.visit(node)
        }
    }
}
