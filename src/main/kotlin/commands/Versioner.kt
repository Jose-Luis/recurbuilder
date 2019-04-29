package commands

import info.Info
import node.visitors.DepsVersionChanger
import node.visitors.VersionChanger

class Versioner
    (
    private val nodename: String,
    private val version: String,
    val info: Info
) {
    fun updateVersion() {
        info.projects.get(nodename).acceptVisitor(VersionChanger(info, version))
        info.projects.serialVisit { it.dependsOn(nodename) }.with(DepsVersionChanger(info, nodename, version))
    }
}