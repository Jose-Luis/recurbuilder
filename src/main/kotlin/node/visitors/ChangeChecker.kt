package node.visitors

import node.Node
import node.StatusCache
import node.output
import node.runCommand

class ChangeChecker(val cache: StatusCache) : NodeVisitor {
    override fun visit(node: Node) {
        node.setFlag(
            "dirty",
            node.hasFlag("updated") && cache.isChange(node.name, CHANGES_COMMAND.runCommand(node.dir).output())
        )
    }
}
