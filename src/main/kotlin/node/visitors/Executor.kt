package node.visitors

import info.Info
import node.Node
import tools.Commander

class Executor(val info: Info, val command: String) : NodeVisitor {
    override fun visit(node: Node) {
        println("\t========= Executing command -> $command on ${node.dir.canonicalPath}")
        Commander().of(command).onDir(info.workspace.resolve(node.dir)).verbose(true).run()
    }
}