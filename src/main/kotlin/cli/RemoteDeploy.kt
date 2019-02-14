package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import commands.RemoteDeployer
import info.Info
import java.io.File

class RemoteDeploy :
    CliktCommand(
        help = "Clone, checkout a branch, compile, deploy on a server and clean the folder",
        name = "rdeploy"
    ) {
    private val projectname by argument()
    private val server by argument()
    private val branch by option("-b", "--branch").default("development")
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    private val skipTests by option("-u", "--withoutTests").flag()
    private val workspace by option("-w", "--workspace").file(exists = true)

    override fun run() {
        val info = Info(infoFile, workspace)
        val env = when (this.server) {
            "des01", "des02", "des03" -> "dev"
            "pre01", "pre02", "pre03" -> "pre"
            else -> throw NotImplementedError()
        }
        RemoteDeployer(projectname, server, branch, env, skipTests, info).deploy()
    }
}