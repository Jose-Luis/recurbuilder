package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import info.Info
import java.io.File
import kotlin.system.exitProcess

class Deploy :
    CliktCommand(help = "Deploy a project on a server") {
    private val projectname by argument()
    private val server by argument()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    override fun run() {
        val info = Info(infoFile)
        val server = info.servers[server]!!
        val app = info.apps[projectname]!!
        val project = info.projects[projectname]
        System.out.println("===BACKUP")
        info.SSHClient.backup(server, app)
        System.out.println("===UNDEPLOY")
        info.SSHClient.delete(server, app)
        info.SSHClient.waitUntilUndeploy(server, app)
        System.out.println("===DEPLOY")
        info.SSHClient.upload(server, app, project)
        exitProcess(0)
    }
}