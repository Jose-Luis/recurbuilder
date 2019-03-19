package commands

import docker.DockerMachine
import info.Info
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class DockerManager(val info: Info) {
    companion object {
        val dockerFolder = File("../docker")
        val dockerOutput = dockerFolder.resolve("servers.log")
    }

    fun startProxy(serverName: String, redirections: String) {
        val server = info.servers[serverName]
        DockerMachine.PROXY.start(
            "proxy", mapOf(
                "REDIRECTIONS" to redirections,
                "URL" to getUrl(serverName), "IP" to server!!.ip
            )
        )
    }

    private fun startService(env: String, service: String) {
        val project = info.projects[service]
        val warname = info.workspace.resolve(project.target).name
        val servicename = info.workspace.resolve(project.target).nameWithoutExtension
        DockerMachine.SERVICE.start(
            servicename, mapOf(
                "CONFIG_FOLDER" to dockerFolder.resolve("tomcat").resolve("config/$env").absolutePath,
                "WAR_URL" to info.workspace.resolve(project.target).absolutePath,
                "WAR_FILE" to warname,
                "PORT" to info.apps.getValue(service).port,
                "DEBUG_PORT" to (Integer.valueOf(info.apps.getValue(service).port) + 1000).toString()
            )
        )
    }

    fun startServices(env: String, services: String) =
        services.trim().split("+").forEach { startService(env, it.trim()) }

    fun startProxies() {
        info.proxies.values.forEach {
            DockerMachine.INTERNAL_PROXY.start(
                it.name,
                mapOf(
                    "SERVICE_URL" to it.url,
                    "SERVICE_IP" to it.ip,
                    "SERVICE_PORT" to it.port,
                    "WEB_PROXY" to info.proxyConnection
                )
            )
        }
    }

    fun stopProxy() = DockerMachine.PROXY.stop("proxy")

    fun stopServices(services: String) = services.trim().split("+").forEach { DockerMachine.SERVICE.stop(it) }

    fun stopProxies() = info.proxies.keys.forEach { DockerMachine.INTERNAL_PROXY.stop(it) }

    fun readBuffer() {
        val bufferedReader = BufferedReader(FileReader(dockerOutput));
        while (true) {
            if (bufferedReader.ready()) {
                val line = bufferedReader.readLine();
                System.out.println(line);
            }
        }
    }
}

