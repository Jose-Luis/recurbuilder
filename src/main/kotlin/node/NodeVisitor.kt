package node

import kotlin.system.exitProcess

private const val GIT_STATUS = "git status --porcelain"
private const val GIT_PULL = "git pull origin development"
private const val CHANGES_COMMAND = GIT_STATUS;
private const val MVN = "mvn -T 4 clean install -PskipSiebelTests -DskipTests"

interface NodeVisitor {
    fun visit(node: Node)
}

class DirtyPrinter : NodeVisitor {
    override fun visit(node: Node) {
        if (node.hasOrInheritFlag("dirty") && !node.hasFlag("printed")) {
            System.out.println("== ${node.name.toUpperCase()} will be built")
            GIT_STATUS.run(node.url)
            node.flag("printed")
        }
    }
}

class Updater : NodeVisitor {
    override fun visit(node: Node) {
        if (!node.hasFlag("update-checked")) {
            node.setFlag("updated", !GIT_PULL.runCommand(node.url).output().isBlank())
            node.flag("update-checked")
        }
    }
}

class CacheUpdater(val cache: StatusCache) : NodeVisitor {
    override fun visit(node: Node) {
        cache.updateCache(node.name, CHANGES_COMMAND.runCommand(node.url).output())
    }
}

class ChangeChecker(val cache: StatusCache) : NodeVisitor {
    override fun visit(node: Node) {
        node.setFlag(
            "dirty",
            node.hasFlag("updated") && cache.isChange(node.name, CHANGES_COMMAND.runCommand(node.url).output())
        )
    }
}

class DirtyBuilder : NodeVisitor {
    override fun visit(node: Node) {
        if (node.hasOrInheritFlag("dirty") && !node.hasFlag("built")) {
            if (MVN.run(node.url).exitValue() == 0) {
                node.flag("built")
            } else {
                exitProcess(33)
            }
        }
    }
}
