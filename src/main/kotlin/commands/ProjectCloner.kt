package commands

import info.Info
import node.Node
import node.visitors.*

class ProjectCloner(val info: Info, val project: Node) {
    fun clone() {
        project.acceptVisitor(Cloner(info))
    }
}