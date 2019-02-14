package commands

import info.Info
import node.visitors.ServerDeployer
import java.io.File
import kotlin.system.exitProcess

class LocalDeployer(
    val projectname: String,
    val server: String,
    val noBackup: Boolean,
    val info: Info
) {
    fun deploy() {
        val server = info.servers[server]!!
        val app = info.apps[projectname]!!
        val project = info.projects[projectname]
        if (!noBackup) {
            Downloader(app.name, server.name, info).download();
        }
        project.acceptVisitor(ServerDeployer(server, app, info))
    }
}