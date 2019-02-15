package node.visitors

import info.Info
import node.Node
import tools.Commander

class Cloner(val info: Info) : NodeVisitor {
    override fun visit(node: Node) {
        Commander().of(info.commands.clone)
            .param("REMOTE_URL", node.remote)
            .param("DESTINATION_DIR", node.dir.name)
            .onDir(info.workspace).verbose(true).run()
    }
}