package node.visitors

import info.Info
import node.Node
import tools.Commander

class BranchSwitcher(val info: Info, val branch: String) : NodeVisitor {
    override fun visit(node: Node) {
        Commander().of("git checkout $branch").onDir(info.workspace.resolve(node.dir.name)).verbose(true).run()
    }
}