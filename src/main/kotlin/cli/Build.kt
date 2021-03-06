package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import commands.Builder
import java.io.File

class Build() :
    CliktCommand(help = "Build the project and its dependencies") {
    private val nodename by argument()
    private val env by option("-e", "--env").choice("int", "dev", "pre", "pro").default("dev")
    private val noTests by option("-u", "--withoutTests").flag()
    private val children by option("-c", "--children").flag()
    private val force by option("-f", "--forceAll").flag()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    override fun run() = Builder(nodename, env, noTests, children, force, infoFile).build()
}

