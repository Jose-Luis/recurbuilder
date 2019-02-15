package node.visitors

import info.Info
import info.entities.App
import info.entities.Server
import node.Node
import tools.Commander
import tools.SSHClient
import java.io.File

class ServerDeployer(val server: Server, val app: App, val info: Info) : NodeVisitor {
    override fun visit(node: Node) {
        try {
            System.out.println("===UNDEPLOY")
            info.SSHClient.delete(server, app)
        } finally {
            info.SSHClient.waitUntilUndeploy(server, app)
            System.out.println("===DEPLOY")
            info.SSHClient.upload(server, app, node, info.workspace)
        }
    }
}