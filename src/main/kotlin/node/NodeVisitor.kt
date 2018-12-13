package node

import kotlin.system.exitProcess

private const val GIT_STATUS = "git status --porcelain"
private const val GIT_PULL = "git pull origin development"
private const val CHANGES_COMMAND = GIT_STATUS;
private const val DOWNLOAD_COMMAND = "git clone";
private const val MVN = "mvn -T 4 clean install -Pdevelopment -DskipTests"

interface NodeVisitor {
    fun visit(node: Node)
}

class DirtyPrinterVisitor : NodeVisitor {
    override fun visit(node: Node) {
        if (node.isDirty()) {
            System.out.println("================== ${node.name.toUpperCase()} ==========================")
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

class RetarderVisitor : NodeVisitor {
    override fun visit(node: Node) {
        node.treated = true
    }
}

class DownloaderVisitor : NodeVisitor {
    override fun visit(node: Node) {
        node.dirty = !DOWNLOAD_COMMAND.plus(" ${node.repoUrl} ${node.url}").runCommand(node.repoUrl).output().isBlank();
    }
}

class BuilderVisitor : NodeVisitor {
    override fun visit(node: Node) {
        if (MVN.run(node.url).exitValue() == 0) {
            node.treated = true
        } else {
            exitProcess(33)
        }
    }

}

class DirtyBuilderVisitor : NodeVisitor {
    override fun visit(node: Node) {
        if (node.isDirty()) {
            BuilderVisitor().visit(node)
        }
    }
}
