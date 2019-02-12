package node.visitors

import tools.Commander
import node.Node

class Printer(val statusCommand: String) : NodeVisitor {
    override fun visit(node: Node) {
        if (!node.hasFlag("printed")) {
            System.out.println(String.format("=================================================================="))
            System.out.println(String.format("===================== %-25s =====================", node.name.toUpperCase()))
            System.out.println(String.format("=================================================================="))
            node.flag("printed")
        }
    }
}
