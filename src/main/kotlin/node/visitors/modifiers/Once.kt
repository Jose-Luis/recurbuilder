package node.visitors.modifiers

import node.Node
import node.visitors.NodeVisitor
import java.util.*

class Once(val visitor: NodeVisitor) : NodeVisitor {
    val visitedFlag = UUID.randomUUID().toString()
    override fun visit(node: Node) {
        IfNotFlaggedAs(visitedFlag, visitor).visit(node)
        node.flag(visitedFlag)
    }
}
