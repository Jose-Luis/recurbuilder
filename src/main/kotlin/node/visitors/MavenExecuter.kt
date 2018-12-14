package node.visitors

import node.Node
import node.run
import kotlin.system.exitProcess

class MavenExecuter(buildParams: String) : NodeVisitor {

    private val MVN = "mvn -T 4 clean install $buildParams"

    override fun visit(node: Node) {
        if (!node.hasFlag("built")) {
            if (MVN.run(node.url).exitValue() == 0) {
                node.flag("built")
            } else {
                exitProcess(33)
            }
        }
    }
}
