package node.visitors

import info.Info
import node.Node
import tools.Commander

class Updater(val info: Info) : NodeVisitor {
    override fun visit(node: Node) {
        System.out.println("===UPDATING ${node.name}")
        Commander().of(info.commands.update).onDir(info.workspace.resolve(node.dir)).verbose(true).run()
    }
}
