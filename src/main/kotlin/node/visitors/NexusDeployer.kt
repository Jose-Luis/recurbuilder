package node.visitors

import info.Info
import node.Node
import tools.Commander
import kotlin.system.exitProcess

class NexusDeployer(val info: Info, val env: String) : NodeVisitor {
    override fun visit(node: Node) {
        val dir = info.workspace.resolve(node.dir)
        val result = Commander().of(info.commands.`nexus-deploy`)
            .param("ENV", env)
            .onDir(dir).verbose(true).run()
        if (result.exitCode != 0)
            exitProcess(result.exitCode)
        node.flag("deployed")
    }
}