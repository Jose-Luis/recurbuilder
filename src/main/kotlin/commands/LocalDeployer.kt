package commands

import info.Info
import node.Node.Companion.only
import node.visitors.ServerDeployer

class LocalDeployer(
    val projectname: String,
    val server: String,
    val noBackup: Boolean,
    val info: Info
) {
    fun deploy() {
        val server = info.servers[server]!!
        val app = info.apps[projectname]!!
        if (!noBackup) {
            Downloader(app.name, server.name, info).download();
        }
        info.projects.visit(only(projectname)).with(ServerDeployer(server, app, info))
    }
}