package node.visitors

import node.Node
import node.run
import java.time.LocalDateTime.now

class Printer(val statusCommand: String) : NodeVisitor {
    override fun visit(node: Node) {
        if (!node.hasFlag("printed")) {
            System.out.println("== ${node.name.toUpperCase()} will be built".plus(now()))
            statusCommand.run(node.dir)
            node.flag("printed")
        }
    }
}
