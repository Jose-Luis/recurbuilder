package commands

import info.Info
import tools.ANSI_CYAN
import tools.print
import kotlin.system.exitProcess

class Logger(
    val projectname: String,
    val server: String,
    val regex: String?,
    val info: Info
) {
    fun log() {
        val server = info.servers[server]!!
        val app = info.apps[projectname]!!
        print("===LOG", ANSI_CYAN)
        info.SSHClient.showLog(server, app, regex)
        exitProcess(0)
    }
}