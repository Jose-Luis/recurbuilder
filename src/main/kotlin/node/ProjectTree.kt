package node

import com.beust.klaxon.Klaxon
import java.io.File

data class Project(val name: String, val url: String, val deps: List<String>)

class ProjectTree(basedir: String) {
    operator fun get(nodename: String?): Node {
        return projects[nodename]!!
    }

    private val projects: Map<String, Node>

    init {
        val projectJson = Klaxon().parseArray<Project>(File("projects.json").inputStream())!!
        projects = projectJson
            .filter { File(basedir.plus(File.separator).plus(it.url)).isAbsolute }
            .map { Node(it.name, basedir.plus(it.url)) }
            .associateBy { it.name }
        projectJson.forEach { projects[it.name]!!.deps = it.deps.map(projects::getValue) }
    }
}