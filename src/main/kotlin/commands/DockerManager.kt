package commands

import docker.DockerMachine
import info.Info
import tools.Commander
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
        val targetFolder = info.workspace.resolve(project.target).parent
        val servicename = info.workspace.resolve(project.target).nameWithoutExtension
        val configFolder = dockerFolder.resolve("tomcat").resolve("config")
        DockerMachine.SERVICE.start(
            servicename, mapOf(
                "CONFIG_FOLDER" to configFolder.resolve(getConfigFolder(env)).absolutePath,
                "SERVICE_NAME" to servicename,
                "TARGET_FOLDER" to targetFolder,
                "PORT" to info.apps.getValue(service).port,
                "DEBUG_PORT" to (Integer.valueOf(info.apps.getValue(service).port) + 1000).toString()
            )
        )
    }

    fun startServices(env: String, services: String) {
        val configFolder = dockerFolder.resolve("tomcat").resolve("config")
        cloneOrUpdateConfig(configFolder)
        services.trim().split("+").forEach { startService(env, it.trim()) }
    }

    fun startProxies(env: String) {
        info.proxies.values.filter { it.active }.forEach {
            DockerMachine.INTERNAL_PROXY.start(
                it.name,
                mapOf(
                    "SERVICE_URL" to it.servers.getValue(env).url,
                    "SERVICE_IP" to it.servers.getValue(env).ip,
                    "SERVICE_PORT" to it.servers.getValue(env).port,
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

    private fun cloneOrUpdateConfig(folder: File) {
        if (folder.exists()) {
            Commander().of("git pull origin master").onDir(folder).verbose(true).run()
        } else {
            folder.mkdir()
            Commander().of("git clone ${info.propertiesRepo} .").onDir(folder).verbose(true).run()
        }
    }
}

