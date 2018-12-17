package node.visitors

import info.StatusCache
import node.*

class CacheUpdater(val cache: StatusCache, val cacheCommand: String) : NodeVisitor {
    override fun visit(node: Node) {
        cache.updateCache(node.name, cacheCommand.runCommand(node.dir).output())
    }
}

