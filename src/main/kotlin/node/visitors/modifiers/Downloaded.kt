package node.visitors.modifiers

import info.Info
import node.Node
import node.visitors.NodeVisitor

class Downloaded(val info: Info, val visitor: NodeVisitor) : NodeVisitor {
    override fun visit(node: Node) {
        if (node.isDownloaded(info.workspace))
            visitor.visit(node)
    }
}