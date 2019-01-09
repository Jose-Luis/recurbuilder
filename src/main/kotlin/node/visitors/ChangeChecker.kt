package node.visitors

import tools.DiffCache
import node.Node

class ChangeChecker(val cache: DiffCache, val env: String) : NodeVisitor {
    override fun visit(node: Node) {
        if (node.hasFlag("dirty") || cache.isChange(node, env)) {
            node.flag("dirty")
        }
    }
}
