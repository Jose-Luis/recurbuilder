package commands

import info.Info
import java.io.File
import kotlin.system.exitProcess

class Logger(
    val projectname: String,
    val server: String,
    val infoFile: File
) {
    fun log() {
        val info = Info(infoFile)
        val server = info.servers[server]!!
        val app = info.apps[projectname]!!
        System.out.println("===LOG")
        info.SSHClient.showLog(server, app)
        exitProcess(0)
    }
}