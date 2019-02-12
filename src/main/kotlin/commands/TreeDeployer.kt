package commands

import info.Info
import node.visitors.*
import node.visitors.Deployer
import node.visitors.modifiers.Composed
import node.visitors.modifiers.Flagged
import node.visitors.modifiers.NotFlagged
import node.visitors.modifiers.PreOrder
import java.io.File
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter

class TreeDeployer
    (
    private val nodename: String,
    private val env: String,
    val infoFile: File
) {

    fun deployTree() {
        val info = Info(infoFile)
        val workingDir =
            info.workspace.resolve(DateTimeFormatter.ofPattern("YYYYMMddHHmmss").format(now())).absoluteFile
        if (!workingDir.isDirectory) workingDir.mkdir()
        info.projects.all().filter { it.dependsOn(nodename) }.parallelStream().forEach { node ->
            node.acceptVisitor(
                PreOrder(
                    Composed(
                        ChangeChecker(info.cacheDir, info.commands.`remote-changes`, env),
                        Flagged( "dirty", NotFlagged( "deployed",
                                Composed(
                                    Cloner(workingDir),
                                    Deployer(workingDir, info.commands.deploy, env),
                                    Cleaner(workingDir)
                        ))),
                        CacheUpdater(info.cacheDir, info.commands.`remote-changes`, env)
                    )
                )
            )
        }
    }
}