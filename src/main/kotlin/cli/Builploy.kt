package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import commands.Builder
import commands.LocalDeployer
import commands.Logger
import info.Info
import java.io.File

class Builploy() :
    CliktCommand(help = "Build the project and its dependencies and deploy local file") {
    private val nodename by argument()
    private val server by argument()
    private val skipTests by option("-u", "--withoutTests").flag()
    private val force by option("-f", "--forceAll").flag()
    private val regex by option("-r", "--regex")
    private val noBackup by option("-n", "--no-backups").flag()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("../info.json"))
    private val workspace by option("-w", "--workspace").file(exists = true)
    override fun run() {
        val info = Info(infoFile, workspace)
        val env = when (server) {
            "des01", "des02", "des03" -> "dev"
            "pre01", "pre02", "pre03" -> "pre"
            else -> throw NotImplementedError()
        }
        Builder(nodename, env, skipTests, force, "deps", info).build()
        LocalDeployer(nodename, server, noBackup, info).deploy()
        Logger(nodename, server, regex, info).log()
    }
}

