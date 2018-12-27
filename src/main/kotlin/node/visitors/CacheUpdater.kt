package node.visitors

import info.StatusCache
import node.*

class CacheUpdater(val cache: StatusCache, val env: String) : NodeVisitor {
    override fun visit(node: Node) {
        cache.updateCache(node, env)
    }
}

