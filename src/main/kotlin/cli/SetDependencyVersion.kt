package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import commands.DependencyVersioner
import info.Info
import java.io.File

class SetDependencyVersion() :
    CliktCommand(
        help = "Set external dependency version ",
        name = "dep-version"
    ) {
    private val artifactDescriptor by argument(
        name = "artifact-descriptor",
        help = "groupId:artifactId:version"
    ).multiple()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("../info.json"))
    private val workspace by option("-w", "--workspace").file(exists = true)
    override fun run() {
        val info = Info(infoFile, workspace)
        artifactDescriptor.forEach { DependencyVersioner(it, info).updateVersion() }
    }
}

