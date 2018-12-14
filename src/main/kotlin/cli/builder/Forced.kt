package cli.builder

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import node.ProjectTree
import node.StatusCache
import node.visitors.MavenExecuter
import node.visitors.CacheUpdater
import node.visitors.Printer
import node.visitors.Updater

class Forced(val cache: StatusCache) :
    CliktCommand(help = "Force to compile all deps tree") {
    val nodename by option("-p", "--project")
    val buildParams by option("-b", "--build-params")
    val basedir by argument().file(exists = true)
    override fun run() {
        val projects = ProjectTree(basedir)
        val node = projects[nodename]
        node.acceptVisitorAfterDeps(Updater())
        node.acceptVisitorAfterDeps(Printer())
        node.acceptVisitorAfterDeps(MavenExecuter(buildParams!!))
        node.acceptVisitorAfterDeps(CacheUpdater(cache))
        cache.persist()
    }
}