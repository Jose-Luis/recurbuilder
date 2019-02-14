package node.visitors

import info.Info
import node.Node
import tools.DiffCache

class LocalCacheUpdater(val info: Info, val env: String) : NodeVisitor {
    val cache = DiffCache(info.cacheDir, "local", info.commands.changes)
    override fun visit(node: Node) {
        cache.updateCache(node, "SNAPSHOT", env)
    }
}

