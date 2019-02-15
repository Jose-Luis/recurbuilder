package commands

import info.Info
import node.Node.Companion.only
import node.visitors.Cloner

class ProjectCloner(val info: Info, val project: String) {
    fun clone() = info.projects.visit(only(project)).with(Cloner(info))
}