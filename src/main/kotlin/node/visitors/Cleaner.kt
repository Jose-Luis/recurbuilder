package node.visitors

import info.Info
import node.Node
import tools.Commander
import java.io.File

class Cleaner(val info: Info) : NodeVisitor {
    override fun visit(node: Node) {
        Commander().of("rm -rf ${node.dir.name}").onDir(info.workspace).verbose(true).run()
    }
}