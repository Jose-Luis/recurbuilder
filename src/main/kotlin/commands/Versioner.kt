package commands

import info.Info
import node.visitors.PomVersionChanger

class Versioner
    (
    private val nodename: String,
    private val version: String,
    val info: Info
) {
    fun updateVersion() {
        info.projects.serialVisit { it.dependsOn(nodename) }.with(PomVersionChanger(info, nodename, version))
    }
}