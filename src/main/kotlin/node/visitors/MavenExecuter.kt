package node.visitors

import tools.Commander
import node.Node
import kotlin.system.exitProcess

class MavenExecuter(val buildCommand: String) : NodeVisitor {

    override fun visit(node: Node) {
        if (!node.hasFlag("built")) {
            System.out.println("\t===BUILDING ${node.name}")
            val result = Commander().of(buildCommand).onDir(node.dir).verbose(true).run()
            if (result.isOk()) {
                node.flag("built")
            } else {
                exitProcess(33)
            }
        }
    }
}
