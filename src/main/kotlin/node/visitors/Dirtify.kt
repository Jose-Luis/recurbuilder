package node.visitors

import node.Node

class Dirtify(val visitor: NodeVisitor) : NodeVisitor {
    override fun visit(node: Node) {
        if (node.hasOrInheritFlag("dirty")) {
            visitor.visit(node)
        }
    }
}
