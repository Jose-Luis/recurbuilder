package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import commands.TreeDeployer
import info.Info
import java.io.File

class NexusTreeDeploy() :
    CliktCommand(
        help = "Clone, checkout a branch, compile, deploy on Nexus and clean folder in tree preorder traversal",
        name = "ntdeploy"
    ) {
    private val nodename by argument()
    private val env by option("-e", "--env").choice("int", "dev", "pre", "pro").default("dev")
    private val branch by option("-b", "--branch").required()
    private val skipTests by option("-u", "--withoutTests").flag()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("../info.json"))
    private val workspace by option("-w", "--workspace").file(exists = true)

    override fun run() {
        val info = Info(infoFile, workspace)
        TreeDeployer(nodename, env, skipTests, branch, info).deployTree()
    }
}

