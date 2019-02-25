package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import commands.LocalDeployer
import info.Info
import java.io.File

class LocalDeploy :
    CliktCommand(help = "LocalDeploy a project on a server", name = "deploy") {
    private val projectname by argument()
    private val server by argument()
    private val noBackup by option("-n", "--no-backups").flag()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("../info.json"))
    private val workspace by option("-w", "--workspace").file(exists = true)

    override fun run() {
        val info = Info(infoFile, workspace)
        LocalDeployer(projectname, server, noBackup, info).deploy()
    }
}