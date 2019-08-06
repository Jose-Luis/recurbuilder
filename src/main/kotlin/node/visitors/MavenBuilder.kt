package node.visitors

import info.Info
import node.Node
import tools.*
import kotlin.system.exitProcess

class MavenBuilder(val info: Info, val skipTests: Boolean, val env: String) : NodeVisitor {
    override fun visit(node: Node) {
        print("\t===BUILDING ${node.name}", ANSI_PURPLE)
        val result = Commander().of(info.commands.build)
            .param("ENV", if (env =="stg") "pro" else env)
            .param("SKIP_TESTS", if (skipTests) "-DskipTests" else "")
            .onDir(info.workspace.resolve(node.dir))
            .verbose(true).run()
        if (!result.isOk()) {
            exitProcess(33)
        }
    }
}
