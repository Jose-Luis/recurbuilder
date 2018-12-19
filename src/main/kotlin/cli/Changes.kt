package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import info.Info
import node.Node
import java.io.File
import java.util.function.Predicate
import node.visitors.*
import node.visitors.modifiers.*

class Changes() :
    CliktCommand(help = "Print the changes the project, its dependecies and childs") {
    private val nodename by argument()
    private val cascade by option("-c", "--cascade").flag()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    override fun run() {
        val info = Info(infoFile)
        val filter = if (cascade) Predicate { it.dependsOn(nodename) } else Predicate<Node> { it.name == nodename }
        info.projects.all().filter { filter.test(it) }.parallelStream().forEach { node ->
            node.acceptVisitor(PreOrder(Composed(Printer(info.commands.print))))
        }
    }
}

