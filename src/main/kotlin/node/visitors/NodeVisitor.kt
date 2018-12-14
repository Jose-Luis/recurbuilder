package node.visitors

import node.Node


const val GIT_STATUS = "git status --porcelain"
const val GIT_PULL = "git pull origin development"
const val CHANGES_COMMAND = GIT_STATUS;

interface NodeVisitor {
    fun visit(node: Node)
}



