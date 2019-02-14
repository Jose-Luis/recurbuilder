package node.visitors

import info.Info
import node.Node
import tools.Commander
import kotlin.system.exitProcess

class MavenBuilder(val info: Info, val skipTests: Boolean, val env: String) : NodeVisitor {
    override fun visit(node: Node) {
        System.out.println("\t===BUILDING ${node.name}")
        val result = Commander().of(info.commands.build)
            .param("ENV", env)
            .param("SKIP_TESTS", if (skipTests) "-DskipTests" else "")
            .onDir(info.workspace.resolve(node.dir))
            .verbose(true).run()
        if (!result.isOk()) {
            exitProcess(33)
        }
    }
}
