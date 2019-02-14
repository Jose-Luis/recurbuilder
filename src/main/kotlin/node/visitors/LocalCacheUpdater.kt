package node.visitors

import info.Info
import node.Node
import tools.DiffCache
import tools.LocalCacheReader

class LocalCacheUpdater(val info: Info, val env: String) : NodeVisitor {
    override fun visit(node: Node) {
        val localCacheReader = LocalCacheReader(node, info)
        val cache = DiffCache(info, "local", localCacheReader)
        cache.updateCache(node, "SNAPSHOT", env)
    }
}

