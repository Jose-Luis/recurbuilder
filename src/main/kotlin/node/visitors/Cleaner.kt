package node.visitors

import node.Node
import tools.Commander
import java.io.File

class Cleaner(val workspace: File) : NodeVisitor {
    override fun visit(node: Node) {
        Commander().of("rm -rf ${node.name}").onDir(workspace).verbose(true).run()
    }
}