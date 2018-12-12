package node

import kotlin.system.exitProcess

private const val GIT_STATUS = "git status --porcelain"
private const val GIT_PULL = "git pull origin development"
private const val CHANGES_COMMAND = GIT_STATUS;
private const val MVN = "mvn -T 4 clean install -PskipSiebelTests -DskipTests"

interface NodeVisitor {
    fun visit(node: Node)
}

class DirtyPrinterVisitor : NodeVisitor {
    override fun visit(node: Node) {
        if (node.isDirty()) {
            System.out.println("================== ${node.name.toUpperCase()} ==========================")
            System.out.println("==================        ${node.url}         ==========================")
            GIT_STATUS.run(node.url)
        }
    }
}

class UpdaterVisitor : NodeVisitor {
    override fun visit(node: Node) {
        GIT_PULL.run(node.url)
    }
}

class ChangeCheckerVisitor : NodeVisitor {
    override fun visit(node: Node) {
        node.dirty = !CHANGES_COMMAND.runCommand(node.url).output().isBlank();
    }
}

class DirtyBuilderVisitor : NodeVisitor {
    override fun visit(node: Node) {
        if (node.isDirty()) {
            if (MVN.run(node.url).exitValue() == 0) {
                node.treated = true
            } else {
                exitProcess(33);
            }
        }
    }
}
