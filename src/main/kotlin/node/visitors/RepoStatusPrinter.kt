package node.visitors

import info.Info
import node.Node
import tools.Commander

class RepoStatusPrinter(val info: Info) : NodeVisitor {
    override fun visit(node: Node) {
        Commander().of(info.commands.changes).onDir(info.workspace.resolve(node.dir)).verbose(true).run()
    }
}
