package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import commands.ChangesPrinter
import commands.ClonerAll
import info.Info
import node.Node
import java.io.File
import java.util.function.Predicate
import node.visitors.*
import node.visitors.modifiers.*

class CloneAll() :
    CliktCommand(help = "Print the changes the project, its dependecies and childs") {
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    private val workspace by option("-w", "--workspace").file(exists = true)

    override fun run() {
        val info = Info(infoFile, workspace)
        ClonerAll(info).cloneAll()
    }
}

