package node.visitors

import node.*

class Updater : NodeVisitor {
    override fun visit(node: Node) {
        if (!node.hasFlag("update-checked")) {
            node.setFlag("updated", !GIT_PULL.runCommand(node.dir).output().isBlank())
            node.flag("update-checked")
        }
    }
}
