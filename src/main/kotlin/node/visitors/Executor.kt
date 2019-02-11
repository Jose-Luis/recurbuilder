package node.visitors

import node.Node
import tools.Commander

class Executor(val command: String) : NodeVisitor {
    override fun visit(node: Node) {
        if (!node.hasFlag("executed")) {
            System.out.println(String.format("===================== %-25s =====================", node.name.toUpperCase()))
            System.out.println("\t== Executing command -> $command on ${node.dir.canonicalPath}")
            Commander().of(command).onDir(node.dir).run()
            node.flag("executed")
        }
    }
}