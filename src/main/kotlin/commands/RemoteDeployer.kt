package commands

import info.Info
import node.Node.Companion.only
import node.visitors.*
import node.visitors.modifiers.Composed

class RemoteDeployer(
    private val projectname: String,
    private val server: String,
    private val branch: String,
    private val env: String,
    private val skipTests: Boolean,
    private val info: Info
) {

    fun deploy() {
        val server = info.servers[server]!!
        val app = info.apps[projectname]!!
        info.projects.visit(only(projectname))
            .with(
                Composed(
                    Cloner(info),
                    BranchSwitcher(info, branch),
                    MavenBuilder(info, skipTests, env),
                    ServerDeployer(server, app, info),
                    Cleaner(info)
                )
            )
    }
}