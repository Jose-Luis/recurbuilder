package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import info.Info
import java.io.File
import kotlin.system.exitProcess

class Log : CliktCommand() {
    private val projectname by argument()
    private val server by argument()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    override fun run() {
        val info = Info(infoFile)
        val server = info.servers[server]!!
        val app = info.apps[projectname]!!
        System.out.println("===LOG")
        info.SSHClient.showLog(server, app)
        exitProcess(0)
    }
}