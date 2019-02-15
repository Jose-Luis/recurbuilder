package commands

import info.Info
import node.visitors.*
import node.visitors.modifiers.Composed
import node.visitors.modifiers.OnlyIf
import node.visitors.modifiers.PreOrder

class TreeDeployer
    (
    private val nodename: String,
    private val env: String,
    private val skipTests: Boolean,
    private val branch: String,
    val info: Info
) {

    private val cloneBuildAndDeploy = Composed(
        Printer(),
        Cloner(info),
        BranchSwitcher(info, branch),
        MavenBuilder(info, skipTests, env),
        NexusDeployer(info, env),
        Cleaner(info)
    )

    private val treeDeployer = PreOrder(
        RemoteChangeChecker(info, branch, env),
        OnlyIf("dirty", cloneBuildAndDeploy),
        RemoteCacheUpdater(info, branch, env)
    )

    fun deployTree() = info.projects.visit({ node -> node.dependsOn(nodename) }, treeDeployer)
}