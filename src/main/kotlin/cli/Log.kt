package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import commands.Logger
import info.Info
import java.io.File

class Log :
    CliktCommand(help = "Show the log of a server") {
    private val projectname by argument()
    private val server by argument()
    private val regex by option("-r", "--regex")
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    private val workspace by option("-w", "--workspace").file(exists = true)
    override fun run() {
        val info = Info(infoFile, workspace)
        Logger(projectname, server, regex, info).log()
    }
}