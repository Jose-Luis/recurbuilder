package node

import com.beust.klaxon.Klaxon
import tornadofx.*
import java.io.File


class ProjectTree(dir: File) {
    operator fun get(nodename: String?): Node {
        return projects[nodename]!!
    }

    private val projects: Map<String, Node>

    data class Project(val name: String, val url: String, val deps: List<String>)

    init {
        val projectJson = Klaxon().parseArray<Project>(File("projects.json").inputStream())!!
        projects = projectJson
            .filter { dir.resolve(it.url).isAbsolute }
            .map { Node(it.name, dir.resolve(it.url)) }
            .associateBy { it.name }
        projectJson.forEach { projects[it.name]!!.deps = it.deps.map(projects::getValue) }
    }

    fun nodes() = projects.values.toList().observable()
}