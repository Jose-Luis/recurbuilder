package node.visitors

import node.*

class Updater(val updateCommand: String) : NodeVisitor {
    private val UPDATED_MSG = "Already up to date.\n"
    override fun visit(node: Node) {
        if (!node.hasFlag("updated")) {
            val output = updateCommand.runCommand(node.dir).output()
            if (!output.isBlank() && output != UPDATED_MSG) {
                node.flag("dirty")
            }
            node.flag("updated")
        }
    }
}
