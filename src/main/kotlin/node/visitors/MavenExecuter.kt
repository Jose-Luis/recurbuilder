package node.visitors

import node.Node
import node.run
import kotlin.system.exitProcess

class MavenExecuter(val buildCommand: String) : NodeVisitor {

    override fun visit(node: Node) {
        if (!node.hasFlag("built")) {
            if (buildCommand.run(node.dir).exitValue() == 0) {
                node.flag("built")
            } else {
                exitProcess(33)
            }
        }
    }
}
