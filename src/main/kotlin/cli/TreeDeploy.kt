package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import commands.TreeDeployer
import java.io.File

class TreeDeploy() :
    CliktCommand(help = "Build the project and its dependencies") {
    private val nodename by argument()
    private val env by option("-e", "--env").choice("int", "dev", "pre", "pro").default("dev")
    private val branch by option("-b", "--branch").required()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    override fun run() = TreeDeployer(nodename, env, branch, infoFile).deployTree()
}

