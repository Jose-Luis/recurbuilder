package info

import com.beust.klaxon.Klaxon
import java.io.File

class Info(file: File) {

    data class Project(val name: String, val url: String, val deps: List<String>)

    data class Commands(
        val build: String,
        val update: String,
        val print: String,
        val changes: String
    )

    data class Root(
        val `repos-dir`: String,
        val `cache-file`: String,
        val commands: Commands,
        val projects: List<Project>
    )

    private val root: Root

    val projects: ProjectTree
    val cache: StatusCache
    val commands: Commands

    init {
        root = Klaxon().parse<Root>(file.inputStream())!!
        projects = ProjectTree(root)
        cache = StatusCache(root.`cache-file`)
        commands = root.commands
    }
}
