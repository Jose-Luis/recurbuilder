package commands

import info.Info
import node.Node
import node.visitors.*
import node.visitors.modifiers.Composed
import node.visitors.modifiers.Flagged
import node.visitors.modifiers.PreOrder
import java.io.File
import java.util.function.Predicate

class ClonerAll(val info: Info) {
    fun cloneAll() {
        info.projects.all().parallelStream().forEach { node ->
            node.acceptVisitor(
                Cloner(info)
            )
        }
    }
}