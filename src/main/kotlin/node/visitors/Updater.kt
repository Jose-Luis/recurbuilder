package node.visitors

import info.Info
import tools.Commander
import node.*

class Updater(val info: Info) : NodeVisitor {
    private val UPDATED_MSG = "Already up to date.\n"
    override fun visit(node: Node) {
        if (!node.hasFlag("updated")) {
            val result = Commander().of(info.commands.update).onDir(info.workspace.resolve(node.dir)).run()
            if (!result.output.isBlank() && result.output != UPDATED_MSG) {
                node.flag("dirty")
            }
            System.out.println("\t===UPDATE ${node.name}")
            node.flag("updated")
        }
    }
}
