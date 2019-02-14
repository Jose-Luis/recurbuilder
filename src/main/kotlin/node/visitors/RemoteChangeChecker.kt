package node.visitors

import info.Info
import node.Node
import tools.DiffCache

class RemoteChangeChecker(val info: Info, val branch: String, val env: String) : NodeVisitor {
    val cache = DiffCache(info.cacheDir, "remote", info.commands.`remote-changes`)
    override fun visit(node: Node) {
        if (cache.isChange(node, branch, env)) {
            node.flag("dirty")
        }
    }
}
