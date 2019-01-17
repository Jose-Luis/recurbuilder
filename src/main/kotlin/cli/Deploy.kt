package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import commands.Deployer
import info.Info
import java.io.File
import kotlin.system.exitProcess

class Deploy :
    CliktCommand(help = "Deploy a project on a server") {
    private val projectname by argument()
    private val server by argument()
    private val noBackup by option("-n", "--no-backups").flag()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    override fun run() = Deployer(projectname, server, noBackup, infoFile).deploy()
}