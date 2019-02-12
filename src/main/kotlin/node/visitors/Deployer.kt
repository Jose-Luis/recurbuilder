package node.visitors

import node.Node
import tools.Commander
import java.io.File
import kotlin.system.exitProcess

class Deployer(val workspace: File, val deployCommand: String, val branch: String, val env: String) : NodeVisitor {
    override fun visit(node: Node) {
        val dir = workspace.resolve(node.name)
        Commander().of("git checkout \$BRANCH").param("BRANCH", branch).onDir(dir).verbose(true).run()
        val result = Commander().of(deployCommand).param("ENV", env).onDir(dir).verbose(true).run()
        if (result.exitCode != 0)
            exitProcess(result.exitCode)
        node.flag("deployed")
    }
}