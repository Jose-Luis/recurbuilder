package info

import info.entities.Root
import node.Node
import java.io.File

class ProjectTree(rootInfo: Root) {
    operator fun get(nodename: String?): Node {
        return projects[nodename]!!
    }

    private val projects: Map<String, Node>

    init {
        val dir = File(rootInfo.`repos-dir`)
        projects = rootInfo.projects
            .filter { dir.resolve(it.url).isAbsolute }
            .map { Node(it.name, dir.resolve(it.url), dir.resolve(it.url).resolve(it.target))  }
            .associateBy { it.name }
        rootInfo.projects.forEach { projects[it.name]!!.deps = it.deps.map(projects::getValue) }
    }

    fun all(): Collection<Node> = projects.values
}