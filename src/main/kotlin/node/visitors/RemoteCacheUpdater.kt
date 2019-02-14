package node.visitors

import info.Info
import tools.DiffCache
import node.*
import java.io.File

class RemoteCacheUpdater(val info: Info, val branch: String, val env: String) : NodeVisitor {
    val cache = DiffCache(info.cacheDir, "remote", info.commands.`remote-changes`)
    override fun visit(node: Node) {
        cache.updateCache(node, branch, env)
    }
}

