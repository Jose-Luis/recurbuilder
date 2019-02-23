package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import commands.*
import info.Info
import java.io.File

class ServiceStarter :
    CliktCommand(help = "Compile the projects and start it on a server", name = "up") {
    private val services by argument().multiple()
    private val server by option("-s", "--server")
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    private val workspace by option("-w", "--workspace").file(exists = true)
    override fun run() {
        val info = Info(infoFile, workspace)
        services.forEach { downloadAndBuild(it, getEnv(server!!), info) }
        DockerManager(info).startServices(server!!, services.joinToString { " " })
    }

    private fun downloadAndBuild(nodeName: String, env: String, info: Info) {
        val node = info.projects[nodeName]
        if (!node.isDownloaded(info.workspace)) ProjectCloner(info, nodeName).clone()
        Builder(nodeName, env, true, false, "deps", info).build()
    }
}