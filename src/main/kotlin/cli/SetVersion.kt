package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.switch
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import commands.Builder
import commands.Updater
import commands.Versioner
import info.Info
import java.io.File

class SetVersion() :
    CliktCommand(
        help = "Set version of projects on its dependencies",
        name = "version" ) {
    private val versions by argument().multiple()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("../info.json"))
    private val workspace by option("-w", "--workspace").file(exists = true)
    override fun run() {
        val info = Info(infoFile, workspace)
        versions.forEach { Versioner(it.split(":")[0], it.split(":")[1], info).updateVersion() }
    }
}

