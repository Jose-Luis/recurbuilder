package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import commands.BranchExecutor
import info.Info
import java.io.File

class Execute :
    CliktCommand(help = "Execute a system command on a dependency branch on preorder") {
    private val nodename by argument()
    private val command by option("-c", "--command")
    private val cascasde by option("-d", "--cascade").flag()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("../info.json"))
    private val workspace by option("-w", "--workspace").file(exists = true)
    override fun run() {
        val info = Info(infoFile, workspace)
        BranchExecutor(nodename, cascasde, command!!, info).execute()
    }
}