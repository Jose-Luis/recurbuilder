package node.visitors

import node.*

class Updater(val updateCommand: String) : NodeVisitor {
    override fun visit(node: Node) {
        if (!node.hasFlag("update-checked")) {
            node.setFlag("updated", !updateCommand.runCommand(node.dir).output().isBlank())
            node.flag("update-checked")
        }
    }
}
