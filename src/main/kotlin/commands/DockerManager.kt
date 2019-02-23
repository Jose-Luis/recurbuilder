package commands

import info.Info
import tools.Commander
import java.io.File

class DockerManager(val info: Info) {
    companion object {
        val dockerFolder = File("docker")
        const val NETWORK_NAME = "aeanet"
        const val BUILD_PROXY = "docker build proxy -t proxy"
        const val RUN_PROXY = "docker run -it --rm -p 80:3000 -e SERVICES='\$SERVICES' AEA_ENV=\$AEA_ENV " +
                "--add-host \$URL:\$IP --net $NETWORK_NAME --name proxy proxy"
        const val BUILD_SERVICE = "docker build tomcat -t tomcat"
        const val RUN_SERVICE = "docker run -it --rm -p 8080:8080 -e WAR_URL=\$WAR_URL CONFIG_FOLDER=\$CONFIG_FOLDER " +
                "--net $NETWORK_NAME --name \$SERVICE tomcat"
    }

    private fun isNetCreated() = Commander().of("docker network ls").run().output.contains(NETWORK_NAME)

    private fun createNetwork() = Commander().of("docker network create $NETWORK_NAME").run()

    private fun startProxy(serverName: String, services: String) {
        val server = info.servers[serverName]
        Commander().of(BUILD_PROXY).onDir(dockerFolder).run()
        Commander().of(RUN_PROXY)
            .param("SERVICES", services)
            .param("AEA_ENV", getEnv(serverName))
            .param("URL", getUrl(serverName))
            .param("IP", server!!.ip)
            .start()
    }

    private fun startService(serverName: String, service: String) {
        val project = info.projects[service]
        Commander().of(BUILD_SERVICE).onDir(dockerFolder).run()
        Commander().of(RUN_SERVICE)
            .param("CONFIG_FOLDER", "config/${getEnv(serverName)}")
            .param("WAR_URL", project.target.absolutePath)
            .param("SERVICE", service)
            .start()
    }

    fun startServices(serverName: String, services: String) {
        if (!isNetCreated()) createNetwork()
        startProxy(serverName, services)
        services.trim().split(" ").forEach { startService(serverName, it.trim()) }
    }
}