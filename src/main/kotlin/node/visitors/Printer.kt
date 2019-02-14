package node.visitors

import node.Node

class Printer() : NodeVisitor {
    override fun visit(node: Node) {
        println("=====================================================================")
        println(String.format("===================== %-25s =====================", node.name.toUpperCase()))
        println("=====================================================================")
    }
}
