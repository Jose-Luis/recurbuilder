package node.visitors

import info.StatusCache
import node.Node

class ChangeChecker(val cache: StatusCache, val env: String) : NodeVisitor {
    override fun visit(node: Node) {
        if (node.hasFlag("dirty") || cache.isChange(node, env)) {
            node.flag("dirty")
        }
    }
}
