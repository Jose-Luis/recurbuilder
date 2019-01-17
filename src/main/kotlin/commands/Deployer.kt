package commands

import info.Info
import java.io.File
import kotlin.system.exitProcess

class Deployer(
    val projectname: String,
    val server: String,
    val noBackup: Boolean,
    val infoFile: File
) {
    fun deploy() {
        val info = Info(infoFile)
        val server = info.servers[server]!!
        val app = info.apps[projectname]!!
        val project = info.projects[projectname]
        if (!noBackup) {
            Downloader(app.name, server.name, infoFile).download();
        }
        System.out.println("===UNDEPLOY")
        info.SSHClient.delete(server, app)
        info.SSHClient.waitUntilUndeploy(server, app)
        System.out.println("===DEPLOY")
        info.SSHClient.upload(server, app, project)
    }
}