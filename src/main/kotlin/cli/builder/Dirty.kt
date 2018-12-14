package cli.builder

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import node.ProjectTree
import node.StatusCache
import node.visitors.*
import node.visitors.MavenExecuter

class Dirty(val cache: StatusCache) :
    CliktCommand(help = "Compile only if necessary") {
    private val nodename by option("-p", "--project")
    private val buildParams by option("-b", "--build-params")
    private val basedir: String by argument()
    override fun run() {
        val projects = ProjectTree(basedir)
        val node = projects[nodename]
        node.acceptVisitorAfterDeps(Updater())
        node.acceptVisitorAfterDeps(ChangeChecker(cache))
        node.acceptVisitorAfterDeps(Dirtify(Printer()))
        node.acceptVisitorAfterDeps(Dirtify(MavenExecuter(buildParams!!)))
        node.acceptVisitorAfterDeps(CacheUpdater(cache))
        cache.persist()
    }

}