package commands

import info.Info
import node.visitors.*
import node.visitors.NexusDeployer
import node.visitors.modifiers.Composed
import node.visitors.modifiers.Flagged
import node.visitors.modifiers.NotFlagged
import node.visitors.modifiers.PreOrder

class TreeDeployer
    (
    private val nodename: String,
    private val env: String,
    private val skipTests: Boolean,
    private val branch: String,
    val info: Info
) {
    fun deployTree() {
        info.projects.all().filter { it.dependsOn(nodename) }.parallelStream().forEach { node ->
            node.acceptVisitor(
                PreOrder(
                    Composed(
                        RemoteChangeChecker(info, branch, env),
                        Flagged(
                            "dirty",
                            NotFlagged(
                                "deployed",
                                Composed(
                                    Printer(),
                                    Cloner(info),
                                    BranchSwitcher(info, branch),
                                    MavenBuilder(info, skipTests, env),
                                    NexusDeployer(info, env),
                                    Cleaner(info)
                                )
                            )
                        ),
                        RemoteCacheUpdater(info, branch, env)
                    )
                )
            )
        }
    }
}