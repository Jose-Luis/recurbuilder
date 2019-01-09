package node.visitors

import tools.DiffCache
import node.*

class CacheUpdater(val cache: DiffCache, val env: String) : NodeVisitor {
    override fun visit(node: Node) {
        cache.updateCache(node, env)
    }
}

