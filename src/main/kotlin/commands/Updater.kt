package commands

import info.Info
import node.visitors.Printer
import node.visitors.Updater
import node.visitors.modifiers.Composed

class Updater(val info: Info) {
    fun update() = info.projects.all().forEach { it.acceptVisitor(Composed(Printer(), Updater(info))) }
}