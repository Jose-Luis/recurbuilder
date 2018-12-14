package node.visitors

import node.*

class CacheUpdater(val cache: StatusCache) : NodeVisitor {
    override fun visit(node: Node) {
        cache.updateCache(node.name, CHANGES_COMMAND.runCommand(node.dir).output())
    }
}

