package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import commands.Builder
import commands.Updater
import info.Info
import java.io.File

class Build() :
    CliktCommand(help = "Build the project and its dependencies") {
    private val nodename by argument()
    private val env by option("-e", "--env").choice("int", "dev", "pre", "pro").default("dev")
    private val skipTests by option("-u", "--withoutTests").flag()
    private val force by option("-f", "--forceAll").flag()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    private val mode by option("-m", "--mode").choice("deps", "cascade", "alone").default("deps")
    private val workspace by option("-w", "--workspace").file(exists = true)
    override fun run() {
        val info = Info(infoFile, workspace)
        Builder(nodename, env, skipTests, force, mode, info).build()
    }
}

