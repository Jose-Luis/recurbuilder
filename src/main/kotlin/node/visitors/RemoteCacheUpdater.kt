package node.visitors

import info.Info
import tools.DiffCache
import node.*
import tools.Commander
import tools.RemoteCacheReader
import java.io.File


class RemoteCacheUpdater(val info: Info, val branch: String, val env: String) : NodeVisitor {
    override fun visit(node: Node) {
        val remoteCacheReader = RemoteCacheReader(node, branch, info)
        val cache = DiffCache(info, "remote", remoteCacheReader)
        cache.updateCache(node, branch, env)
    }
}

