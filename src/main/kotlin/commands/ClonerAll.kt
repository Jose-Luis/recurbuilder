package commands

import info.Info
import node.visitors.*

class ClonerAll(val info: Info) {
    fun cloneAll() {
        info.projects.all().parallelStream().forEach { node ->
            node.acceptVisitor(
                Cloner(info)
            )
        }
    }
}