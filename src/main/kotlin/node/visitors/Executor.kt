package node.visitors

import info.Info
import node.Node
import tools.Commander

class Executor(val info: Info, val command: String) : NodeVisitor {
    override fun visit(node: Node) {
        println("=========================================================================================")
        println("          Executing command for      ${node.name.toUpperCase()}")
        println("=========================================================================================")
        Commander().of(command).onDir(info.workspace.resolve(node.dir)).verbose(true).run()
    }
}