package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import commands.Builder
import commands.DockerManager
import commands.ProjectCloner
import commands.getEnv
import info.Info
import java.io.File

class ServiceStarter :
    CliktCommand(help = "Compile the projects and start it on a proxiedServer", name = "up") {
    private val services by argument().multiple()
    private val env by option("-e", "--environment").choice("dev", "pre")
    private val proxiedServer by option("-p", "--proxy")
    private val redirections by option("-r", "--redirections").multiple()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("../info.json"))
    private val workspace by option("-w", "--workspace").file(exists = true)
    override fun run() {
        val info = Info(infoFile, workspace)
        val hasProxy = !proxiedServer.isNullOrBlank()
        val servicesJoint = services.joinToString("+")
        val servicesRedirections =
            services.map { info.workspace.resolve(info.projects[it].target).nameWithoutExtension }
                .joinToString("+") { "$it->$it:8080" }
        val allRedirections = redirections.joinToString("+") + "+" + servicesRedirections
        val env = if (hasProxy) getEnv(proxiedServer!!) else env ?: "dev"
        println("=== Using profile -> ${env.toUpperCase()}")
        services.forEach { downloadAndBuild(it, env, info) }
        val dockerManager = DockerManager(info)
        Runtime.getRuntime().addShutdownHook(Thread {
            dockerManager.stopServices(servicesJoint)
            if (hasProxy) dockerManager.stopProxy()
        })
        dockerManager.startServices(env, servicesJoint)
        if (hasProxy) dockerManager.startProxy(proxiedServer!!, allRedirections)
        dockerManager.readBuffer()

    }


    private fun downloadAndBuild(nodeName: String, env: String, info: Info) {
        val node = info.projects[nodeName]
        if (!node.isDownloaded(info.workspace)) ProjectCloner(info, nodeName).clone()
        Builder(nodeName, env, true, false, "deps", info).build()
    }
}