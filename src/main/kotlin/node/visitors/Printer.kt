package node.visitors

import node.Node
import node.run

class Printer(val statusCommand: String) : NodeVisitor {
    override fun visit(node: Node) {
        if (!node.hasFlag("printed")) {
            System.out.println(String.format("=============================== %-25s ===============================", node.name.toUpperCase()))
            statusCommand.run(node.dir)
            node.flag("printed")
        }
    }
}
