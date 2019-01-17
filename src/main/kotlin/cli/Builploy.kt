package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import commands.Builder
import commands.Deployer
import commands.Logger
import java.io.File

class Builploy() :
    CliktCommand(help = "Build the project and deploy") {
    private val nodename by argument()
    private val server by argument()
    private val noTests by option("-u", "--withoutTests").flag()
    private val force by option("-f", "--forceAll").flag()
    private val noBackup by option("-n", "--no-backups").flag()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    override fun run() {
        val env = when (server) {
            "des01", "des02", "des03" -> "dev"
            "pre01", "pre02", "pre03" -> "pre"
            else -> throw NotImplementedError()
        }
        Builder(nodename, env, noTests, false, force, infoFile).build()
        Deployer(nodename, server, noBackup, infoFile).deploy()
        Logger(nodename, server, infoFile).log()
    }
}

