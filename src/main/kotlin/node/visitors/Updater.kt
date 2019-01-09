package node.visitors

import tools.Commander
import node.*

class Updater(val updateCommand: String) : NodeVisitor {
    private val UPDATED_MSG = "Already up to date.\n"
    override fun visit(node: Node) {
        if (!node.hasFlag("updated")) {
            val result = Commander().of(updateCommand).onDir(node.dir).run()
            if (!result.output.isBlank() && result.output != UPDATED_MSG) {
                node.flag("dirty")
            }
            System.out.println("\t===UPDATE ${node.name}")
            node.flag("updated")
        }
    }
}
