package node.visitors

import tools.DiffCache
import node.Node
import java.io.File

class ChangeChecker(cacheFile: File, command: String, val branch: String, val env: String) : NodeVisitor {
    val cache = DiffCache(cacheFile, command)
    override fun visit(node: Node) {
        if (cache.isChange(node, branch, env)) {
            node.flag("dirty")
        }
    }
}
