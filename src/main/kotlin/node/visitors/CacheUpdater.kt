package node.visitors

import tools.DiffCache
import node.*
import java.io.File

class CacheUpdater(cacheFile: File, command: String, val env: String) : NodeVisitor {
    val cache = DiffCache(cacheFile, command)
    override fun visit(node: Node) {
        cache.updateCache(node, env)
    }
}

