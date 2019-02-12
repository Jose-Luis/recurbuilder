package node.visitors

import node.Node
import tools.Commander
import java.io.File

class Cloner(val workspace: File) : NodeVisitor {
    override fun visit(node: Node) {
        Commander().of("git clone ${node.remote} ${node.name}").onDir(workspace).verbose(true).run()
    }
}