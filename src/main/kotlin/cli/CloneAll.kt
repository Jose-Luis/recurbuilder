package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import commands.ProjectCloner
import info.Info
import java.io.File

class CloneAll() :
    CliktCommand(help = "Print the changes the project, its dependecies and childs") {
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    private val workspace by option("-w", "--workspace").file(exists = true)

    override fun run() {
        val info = Info(infoFile, workspace)
        info.projects.all().parallelStream().forEach { ProjectCloner(info, it).clone() }
    }
}

