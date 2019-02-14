package node.visitors

import info.Info
import tools.Commander
import node.Node

class RepoStatusPrinter(val info: Info) : NodeVisitor {
    override fun visit(node: Node) {
        if (!node.hasFlag("printed")) {
            System.out.println(String.format("====================================================================="))
            System.out.println(String.format("===================== %-25s =====================", node.name.toUpperCase()))
            System.out.println(String.format("====================================================================="))
            Commander().of(info.commands.changes).onDir(info.workspace.resolve(node.dir).absoluteFile).run()
            System.out.println(String.format("====================================================================="))
            node.flag("printed")
        }
    }
}
