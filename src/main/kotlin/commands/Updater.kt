package commands

import info.Info
import node.visitors.Printer
import node.visitors.Updater
import node.visitors.modifiers.Composed
import node.visitors.modifiers.Downloaded

class Updater(val info: Info) {
    fun update() {
        info.projects.all().forEach { it.acceptVisitor(Downloaded(info, Composed(Printer(), Updater(info)))) }
        info.cacheDir.deleteRecursively()
    }
}