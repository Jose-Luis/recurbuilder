package node.visitors

import node.Node
import tools.ANSI_BG_BLUE
import tools.ANSI_PURPLE
import tools.print

class Printer() : NodeVisitor {
    override fun visit(node: Node) {
        val nodename = String.format("%-25s", node.name.toUpperCase())
        print("=====================================================================", ANSI_PURPLE)
        print("===================== $nodename =====================", ANSI_PURPLE)
        print("=====================================================================", ANSI_PURPLE)
    }
}
