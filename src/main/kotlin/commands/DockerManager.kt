package commands

import docker.DockerMachine
import info.Info
import tools.ANSI_BRIGHT_GREEN
import tools.Commander
import tools.print
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class DockerManager(val info: Info) {
    companion object {
        val dockerFolder = File("../docker")
        val dockerOutput = dockerFolder.resolve("servers.log")
    }

    fun startProxy(serverName: String, redirections: String, editions: String) {
        val server = info.servers[serverName]
        DockerMachine.PROXY.start(
            "proxy", mapOf(
                "REDIRECTIONS" to redirections,
                "EDITIONS" to editions,
                "URL" to getUrl(serverName), "IP" to server!!.ip
            )
        )
    }

    fun startNginx() {
        DockerMachine.NGINX.start("nginx_https", emptyMap())
    }

    fun startService(env: String, service: String) {
        val project = info.projects[service]
        val warname = info.workspace.resolve(project.target).name
        val servicename = info.workspace.resolve(project.target).nameWithoutExtension
        val configFolder = dockerFolder.resolve("tomcat").resolve("config")
        DockerMachine.SERVICE.start(
            servicename, mapOf(
                "CONFIG_FOLDER" to configFolder.resolve(getConfigFolder(env)).absolutePath,
                "WAR_URL" to info.workspace.resolve(project.target).absolutePath,
                "WAR_FILE" to warname,
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

    fun stopNginx() = DockerMachine.NGINX.stop("nginx_https")

    fun stopServices(services: String) {
        services.trim().split("+").map {
            val project = info.projects[it]
            info.workspace.resolve(project.target).nameWithoutExtension
        }.forEach { DockerMachine.SERVICE.stop(it) }
    }

    fun stopProxies() = info.proxies.keys.forEach { DockerMachine.INTERNAL_PROXY.stop(it) }

    fun attachProxy() = Commander().of("docker attach proxy").onDir(dockerFolder).startIO()

    fun isMachineUp(machine: DockerMachine) =
        Commander().of("docker ps").onDir(dockerFolder).run().output.contains(machine.imageName)

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
        if (folder.resolve(".git").exists()) {
            Commander().of("git pull origin master").onDir(folder).verbose(true).run()
        } else {
            folder.mkdir()
            Commander().of("git clone ${info.propertiesRepo} .").onDir(folder).verbose(true).run()
        }
        print("==PROPERTIES SYNCHRONIZED \u2713", ANSI_BRIGHT_GREEN)
    }
}

