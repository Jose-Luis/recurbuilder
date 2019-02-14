package node.visitors

import node.Node

class Printer() : NodeVisitor {
    override fun visit(node: Node) {
        if (!node.hasFlag("printed")) {
            System.out.println(String.format("====================================================================="))
            System.out.println(String.format("===================== %-25s =====================", node.name.toUpperCase()))
            System.out.println(String.format("====================================================================="))
            node.flag("printed")
        }
    }
}
