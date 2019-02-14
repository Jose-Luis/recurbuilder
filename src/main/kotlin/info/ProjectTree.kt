package info

import info.entities.Root
import node.Node
import java.io.File

class ProjectTree(rootInfo: Root) {
    operator fun get(nodename: String?): Node {
        return projects[nodename]!!
    }

    private val projects: Map<String, Node> = rootInfo.projects.map { Node(it) }.associateBy { it.name }

    init {
        rootInfo.projects.forEach { projects[it.name]!!.deps = it.deps.map(projects::getValue) }
    }

    fun all(): Collection<Node> = projects.values
}
