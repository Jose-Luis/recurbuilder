package docker

import tools.Commander
import java.io.File

enum class DockerMachine(private val imageName: String, private val runCommand: String) {
    PROXY(
        "proxy",
        "docker run --rm --net-alias pre.aireuropa.com --net-alias des.aireuropa.com -p 80:80 -e REDIRECTIONS=\$REDIRECTIONS --add-host \$URL:\$IP --net \$NETWORK_NAME --name \$NAME proxy"
    ),
    SERVICE(
        "tomcat",
        "docker run --rm -p \$PORT:8080 -p \$DEBUG_PORT:8000 -v \$WAR_URL:/usr/local/tomcat/webapps/\$WAR_FILE -v \$CONFIG_FOLDER:/config --net \$NETWORK_NAME --name \$NAME tomcat"
    ),
    INTERNAL_PROXY(
        "internal-proxy",
        "docker run --rm --network-alias=\$SERVICE_URL --add-host \$SERVICE_URL:\$SERVICE_IP --network=\$NETWORK_NAME -e SERVICE_URL=\$SERVICE_URL -e SERVICE_PORT=\$SERVICE_PORT -e HTTP_PROXY=\$WEB_PROXY -e HTTPS_PROXY=\$WEB_PROXY --name \$NAME internal-proxy"
    );

    companion object {
        val dockerFolder = File("../docker")
        val dockerOutput = dockerFolder.resolve("servers.log")
        const val NETWORK_NAME = "aeanet"
    }

    fun start(machineName: String, options: Map<String, String>) {
        Commander().of("docker build $imageName -t $imageName").onDir(dockerFolder).verbose(true).run()
        if (!isNetCreated()) createNetwork()
        val dockerCommander = Commander().of(runCommand).onDir(dockerFolder)
        options.forEach { dockerCommander.param(it.key, it.value) }
        dockerCommander.param("NETWORK_NAME", NETWORK_NAME)
        dockerCommander.param("NAME", machineName)
        dockerCommander.start(dockerOutput)
    }

    fun stop(machineName: String) {
        val containers = Commander().of("docker container ls").onDir(dockerFolder).run().output
        if (containers.contains(machineName)) {
            println("===STOPPING $machineName")
            Commander().of("docker kill $machineName").onDir(dockerFolder).verbose(true).run()
        }
    }

    private fun isNetCreated() =
        Commander().of("docker network ls").onDir(dockerFolder).run().output.contains(NETWORK_NAME)

    private fun createNetwork() = Commander().of("docker network create $NETWORK_NAME").onDir(dockerFolder).run()
}