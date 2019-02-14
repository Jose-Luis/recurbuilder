package commands

import info.Info
import node.visitors.*

class ProjectCloner(val info: Info, val project: String) {
    fun clone() {
        info.projects[project].acceptVisitor(Cloner(info))
    }
}