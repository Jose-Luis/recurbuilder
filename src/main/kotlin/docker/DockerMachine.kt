package docker

import tools.*
import java.io.File

enum class DockerMachine(val imageName: String, private val runCommand: String) {
    NGINX(
        "nginx_https",
        "docker run --rm -p 443:443 --net \$NETWORK_NAME --name \$NAME nginx_https"
    ),
    PROXY(
        "proxy",
        "docker run -it --detach-keys=\"X\" --rm -p 80:80 --net-alias des.aireuropa.com --net-alias pre.aireuropa.com --net-alias stg.aireuropa.com  -e REDIRECTIONS=\$REDIRECTIONS -e EDITIONS=\$EDITIONS --add-host \$URL:\$IP --net \$NETWORK_NAME --name \$NAME proxy"
    ),
    SERVICE(
        "tomcat",
        "docker run --rm -p \$PORT:8080 -p \$DEBUG_PORT:8000 -v \$WAR_URL:/usr/local/tomcat/webapps/\$WAR_FILE -v \$CONFIG_FOLDER:/var/config --net \$NETWORK_NAME --name \$NAME tomcat"
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
        print("$machineName \u2713", ANSI_YELLOW)
        Commander().of("docker build $imageName -t $imageName").onDir(dockerFolder).run()
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
            print("===STOPPING $machineName", ANSI_RED)
            Commander().of("docker kill $machineName").onDir(dockerFolder).run()
        }
    }

    private fun isNetCreated() =
        Commander().of("docker network ls").onDir(dockerFolder).run().output.contains(NETWORK_NAME)

    private fun createNetwork() = Commander().of("docker network create $NETWORK_NAME").onDir(dockerFolder).run()
}