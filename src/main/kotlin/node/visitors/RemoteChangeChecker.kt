package node.visitors

import info.Info
import node.Node
import tools.DiffCache
import tools.RemoteCacheReader

class RemoteChangeChecker(val info: Info, val branch: String, val env: String) : NodeVisitor {
    override fun visit(node: Node) {
        val remoteCacheReader = RemoteCacheReader(node, branch, info)
        val cache = DiffCache(info, "remote", remoteCacheReader)
        if (cache.isChange(node, branch, env)) {
            node.flag("dirty")
        }
    }
}
