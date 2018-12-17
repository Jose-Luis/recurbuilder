package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import info.Info
import node.visitors.*
import java.io.File

class Builder() :
    CliktCommand(help = "Build the project") {
    private val nodename by argument()
    private val env by option("-e", "--env").choice("int", "dev", "pre", "pro").default("dev")
    private val noTests by option("-u", "--withoutTests").flag()
    private val force by option("-f", "--forceAll").flag()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    override fun run() {
        val info = Info(infoFile)
        val node = info.projects[nodename]
        val buildCommand = info.commands.build.plus(" -Denv=$env ".plus(if (noTests) "-DskipTests" else ""))
        node.acceptVisitorAfterDeps(Updater(info.commands.update))
        node.acceptVisitorAfterDeps(ChangeChecker(info.cache, info.commands.changes))
        node.acceptVisitorAfterDeps(if (!force) Dirtify(Printer(info.commands.print)) else Printer(info.commands.print))
        node.acceptVisitorAfterDeps(if (!force) Dirtify(MavenExecuter(buildCommand)) else MavenExecuter(buildCommand))
        node.acceptVisitorAfterDeps(CacheUpdater(info.cache, info.commands.update))
        info.cache.persist()
    }
}