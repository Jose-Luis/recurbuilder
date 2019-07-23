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
import docker.DockerMachine
import info.Info
import java.io.File
import java.lang.Thread.sleep

class ServiceStarter :
    CliktCommand(help = "Compile the projects and start it on a proxiedServer", name = "up") {
    private val services by argument().multiple()
    private val env by option("-e", "--environment").choice("dev", "pre", "pro")
    private val proxiedServer by option("-p", "--proxy")
    private val redirections by option("-r", "--redirections").multiple()
    private val editions by option("-ed", "--editions").multiple()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("../info.json"))
    private val workspace by option("-w", "--workspace").file(exists = true)
    override fun run() {
        val info = Info(infoFile, workspace)
        val hasProxy = !proxiedServer.isNullOrBlank()
        val servicesJoint = services.joinToString("+")
        val allRedirections = services.map { info.workspace.resolve(info.projects[it].target).nameWithoutExtension }
            .map { "$it---$it:8080" }.union(this.redirections).joinToString("+") { it.trim() }
        val env = if (hasProxy) getEnv(proxiedServer!!) else env ?: "dev"
        println("=== Using profile -> ${env.toUpperCase()}")
        services.forEach { downloadAndBuild(it, env, info) }
        val dockerManager = DockerManager(info)
        setDownActions(dockerManager, servicesJoint)
        dockerManager.startServices(env, servicesJoint)
        if (hasProxy) dockerManager.startProxy(
            proxiedServer!!,
            allRedirections,
            editions.joinToString("+") { it.trim() })
        while (!dockerManager.isMachineUp(DockerMachine.PROXY)) {
            sleep(200)
        }
        dockerManager.startNginx()
        dockerManager.startProxies(env)
        sleep(1000)
        dockerManager.attachProxy()
    }

    private fun setDownActions(dockerManager: DockerManager, servicesJoint: String) {
        val hasProxy = !proxiedServer.isNullOrBlank()
        Runtime.getRuntime().addShutdownHook(Thread {
            dockerManager.stopServices(servicesJoint)
            if (hasProxy) dockerManager.stopProxy()
            dockerManager.stopNginx()
            dockerManager.stopProxies()
        })
    }


    private fun downloadAndBuild(nodeName: String, env: String, info: Info) {
        val node = info.projects[nodeName]
        if (!node.isDownloaded(info.workspace)) ProjectCloner(info, nodeName).clone()
        Builder(nodeName, env, true, false, "deps", info).build()
    }
}