import com.beust.klaxon.Klaxon
import node.*
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    val projects = getProjectTree(args[0])
    val cache = StatusCache("buildercache")
    val node = projects[args[1]]!!

    node.acceptVisitorAfterDeps(Updater())
    node.acceptVisitorAfterDeps(ChangeChecker(cache))
    node.acceptVisitorAfterDeps(DirtyPrinter())
    node.acceptVisitorAfterDeps(DirtyBuilder())
    node.acceptVisitorAfterDeps(CacheUpdater(cache))
    cache.persist()

    exitProcess(0)
}

data class Project(val name: String, val url: String, val deps: List<String>)

fun getProjectTree(basedir: String): Map<String, Node> {
    val projectJson = Klaxon().parseArray<Project>(File("projects.json").inputStream())!!
    val projects = projectJson
        .filter { File(basedir.plus(it.url)).isAbsolute }
        .map { Node(it.name, basedir.plus(it.url)) }
        .associateBy { it.name }
    projectJson.forEach { projects[it.name]!!.deps = it.deps.map(projects::getValue) }
    return projects
}

