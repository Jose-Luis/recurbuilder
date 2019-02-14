package node.visitors

import info.Info
import node.Node
import tools.Commander
import java.io.File
import kotlin.system.exitProcess

class MavenBuilder(val info: Info, val skipTests: Boolean, val env: String) : NodeVisitor {

    override fun visit(node: Node) {
        if (!node.hasFlag("built")) {
            System.out.println("\t===BUILDING ${node.name}")
            val result = Commander().of(info.commands.build)
                .param("ENV", env)
                .param("SKIP_TESTS", skipTests.toString())
                .onDir(info.workspace.resolve(node.dir))
                .verbose(true).run()
            if (result.isOk()) {
                node.flag("built")
            } else {
                exitProcess(33)
            }
        }
    }
}
