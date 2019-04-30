package commands

import info.Info
import node.visitors.ExternalDepVersionChanger

class DependencyVersioner
    (
    private val artifactDescriptor: String,
    val info: Info
) {
    fun updateVersion() {
        info.projects.all().forEach { it.acceptVisitor(ExternalDepVersionChanger(info, artifactDescriptor)) }
    }
}