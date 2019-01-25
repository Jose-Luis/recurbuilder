package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import commands.Logger
import info.Info
import java.io.File
import kotlin.system.exitProcess

class Log :
    CliktCommand(help = "Show the log of a server") {
    private val projectname by argument()
    private val server by argument()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    private val regex by option("-r", "--regex")
    override fun run() = Logger(projectname, server, regex, infoFile).log()
}