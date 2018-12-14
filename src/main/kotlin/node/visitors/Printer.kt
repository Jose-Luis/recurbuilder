package node.visitors

import node.Node
import node.run

class Printer : NodeVisitor {
    override fun visit(node: Node) {
        if (!node.hasFlag("printed")) {
            System.out.println("== ${node.name.toUpperCase()} will be built")
            GIT_STATUS.run(node.dir)
            node.flag("printed")
        }
    }
}
