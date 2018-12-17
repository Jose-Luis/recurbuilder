package info

import node.Node
import java.io.File

class ProjectTree(rootInfo: Info.Root) {
    operator fun get(nodename: String?): Node {
        return projects[nodename]!!
    }

    private val projects: Map<String, Node>

    init {
        val dir = File(rootInfo.`repos-dir`)
        projects = rootInfo.projects
            .filter { dir.resolve(it.url).isAbsolute }
            .map { Node(it.name, dir.resolve(it.url)) }
            .associateBy { it.name }
        rootInfo.projects.forEach { projects[it.name]!!.deps = it.deps.map(projects::getValue) }
    }
}