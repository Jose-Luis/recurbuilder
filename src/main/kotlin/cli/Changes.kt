package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import commands.ChangesPrinter
import info.Info
import java.io.File

class Changes() :
    CliktCommand(help = "Print the changes the project, its dependecies and childs") {
    private val nodename by argument()
    private val children by option("-c", "--children").flag()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("../info.json"))
    private val workspace by option("-w", "--workspace").file(exists = true)

    override fun run() {
        val info = Info(infoFile, workspace)
        ChangesPrinter(nodename, children, info).printChanges()
    }
}

