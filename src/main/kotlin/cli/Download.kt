package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import commands.Downloader
import info.Info
import java.io.File

class Download :
    CliktCommand(help = "Download the war from a server") {
    private val app by argument()
    private val server by argument()
    private val infoFile by option("-i", "--infoFile").file(exists = true).default(File("info.json"))
    override fun run() = Downloader(app, server, infoFile).download()
}