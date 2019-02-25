package commands

import info.Info
import tools.Commander
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class DockerManager(val info: Info) {
    companion object {
        val dockerFolder = File("../docker")
        val dockerOutput = dockerFolder.resolve("servers.log")
        const val NETWORK_NAME = "aeanet"
        const val BUILD_PROXY = "docker build proxy -t proxy"
        const val RUN_PROXY = "docker run --rm -p 80:3000 -e SERVICES=\$SERVICES -e AEA_ENV=\$AEA_ENV " +
                "--add-host \$URL:\$IP --net $NETWORK_NAME --name proxy proxy"
        const val BUILD_SERVICE = "docker build tomcat -t tomcat"
        const val RUN_SERVICE = "docker run --rm -p \$PORT:8080 -p \$DEBUG_PORT:8000 " +
                "-v \$WAR_URL:/usr/local/tomcat/webapps/\$WAR_FILE -v \$CONFIG_FOLDER:/config --net $NETWORK_NAME --name \$SERVICE tomcat"
    }

    private fun isNetCreated() =
        Commander().of("docker network ls").onDir(dockerFolder).run().output.contains(NETWORK_NAME)

    private fun createNetwork() = Commander().of("docker network create $NETWORK_NAME").onDir(dockerFolder).run()

    fun startProxy(serverName: String, services: String) {
        val server = info.servers[serverName]
        Commander().of(BUILD_PROXY).onDir(dockerFolder).verbose(true).run()
        if (!isNetCreated()) createNetwork()
        Commander().of(RUN_PROXY).onDir(dockerFolder)
            .param("SERVICES", services)
            .param("AEA_ENV", getServerPrefix(serverName))
            .param("URL", getUrl(serverName))
            .param("IP", server!!.ip)
            .start(dockerOutput)
    }

    private fun startService(env: String, service: String) {
        val project = info.projects[service]
        val warname = info.workspace.resolve(project.target).name
        val servicename = info.workspace.resolve(project.target).nameWithoutExtension
        Commander().of(BUILD_SERVICE).onDir(dockerFolder).verbose(true).run()
        Commander().of(RUN_SERVICE).onDir(dockerFolder)
            .param("CONFIG_FOLDER", dockerFolder.resolve("tomcat").resolve("config/$env").absolutePath)
            .param("WAR_URL", info.workspace.resolve(project.target).absolutePath)
            .param("WAR_FILE", warname)
            .param("PORT", info.apps.getValue(service).port)
            .param("DEBUG_PORT", (Integer.valueOf(info.apps.getValue(service).port) + 1000).toString())
            .param("SERVICE", servicename)
            .start(dockerOutput)
    }

    fun startServices(env: String, services: String) {
        if (!isNetCreated()) createNetwork()
        services.trim().split("+").forEach { startService(env, it.trim()) }
    }

    fun readBuffer() {
        val bufferedReader = BufferedReader(FileReader(dockerOutput));
        while (true) {
            if (bufferedReader.ready()) {
                val line = bufferedReader.readLine();
                System.out.println(line);
            }
        }
    }

    fun stopProxy() {
        val containers = Commander().of("docker container ls").onDir(dockerFolder).run().output
        if (containers.contains("proxy")) {
            println("===STOPPING PROXY")
            Commander().of("docker stop proxy").onDir(dockerFolder).verbose(true).run()
        }
    }

    fun stopServices(services: String) {
        println("===STOPPING ALL SERVICES")
        services.trim().split("+").forEach {
            val containers = Commander().of("docker container ls").onDir(dockerFolder).run().output
            if (containers.contains(it)) {
                println("===STOPPING $it")
                Commander().of("docker stop $it").onDir(dockerFolder).verbose(true).run()
            }
        }
    }

    fun color(logLine: String): String {
        return logLine
            .replace("(.* FATAL.*)".toRegex(), "\\\\033[1;31m\\\\$1\\\\033[0;39m")
            .replace("(.* ERROR.*)".toRegex(), "\\\\033[31m\\\\$1\\\\033[39m")
            .replace("(.* WARN.*)".toRegex(), "\\\\033[33m\\\\$1\\\\033[39m")
            .replace("(Caused by)".toRegex(), "\\\\033[33m\\\\$1\\\\033[39m")
            .replace("(.* INFO.*)".toRegex(), "\\033[32m" + "$1" + "\\033[0m")
            .replace("(.* DEBUG.*)".toRegex(), "\\\\033[34m\\\\$1\\\\033[39m")
            .replace("(.* TRACE.*)".toRegex(), "\\\\033[30m\\\\$1\\\\033[39m")
            .replace("(.*at com.globalia.*)".toRegex(), "\\\\033[35m\\\\$1\\\\033[39m")
            .replace("(\\w*.java:[0-9]*)".toRegex(), "\\\\033[1;36m\\\\$1\\\\033[39m")
            .replace("([A-Z]\\w*xception\\b)".toRegex(), "\\\\033[1;33m\\\\$1\\\\033[0;39m")
    }
}

