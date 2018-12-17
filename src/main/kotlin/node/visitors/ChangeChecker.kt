package node.visitors

import node.Node
import info.StatusCache
import node.output
import node.runCommand

class ChangeChecker(val cache: StatusCache, val changeCommand: String) : NodeVisitor {
    override fun visit(node: Node) {
        node.setFlag(
            "dirty",
            node.hasFlag("updated") && cache.isChange(node.name, changeCommand.runCommand(node.dir).output())
        )
    }
}
