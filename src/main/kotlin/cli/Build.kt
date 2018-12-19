package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import info.Info
import node.Node
import java.io.File
import java.util.function.Predicate
import node.visitors.*
import node.visitors.modifiers.*

class Build() :
    CliktCommand(help = "Build the project and its dependencies") {
    private val nodename by argument()
    private val env by option("-e", "--env").choice("int", "dev", "pre", "pro").default("dev")
    private val noTests by option("-u", "--withoutTests").flag()
    private val children by option("-c", "--children").flag()
    private val force by option("-f", "--forceAll").flag()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    override fun run() {
        val info = Info(infoFile)
        val buildCommand = info.commands.build.plus(" -Denv=$env ".plus(if (noTests) "-DskipTests" else ""))
        val projectFilter = if (children) Predicate { it.dependsOn(nodename) } else Predicate<Node> { it.name == nodename }
        info.projects.all().filter { projectFilter.test(it) }.parallelStream().forEach { node ->
            node.acceptVisitor(
                PreOrder(
                    Composed(
                        Updater(info.commands.update),
                        ChangeChecker(info.cache),
                        if (!force) Flagged("dirty", Printer(info.commands.print)) else Printer(info.commands.print),
                        if (!force) Flagged("dirty", MavenExecuter(buildCommand)) else MavenExecuter(buildCommand),
                        CacheUpdater(info.cache)
                    )
                )
            )
        }
    }
}

