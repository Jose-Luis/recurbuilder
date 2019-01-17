package commands

import info.Info
import java.io.File

class Downloader(
    val app: String,
    val server: String,
    val infoFile: File
) {
    fun download() {
        val info = Info(infoFile)
        val server = info.servers[server]!!
        val app = info.apps[app]!!
        System.out.println("===BACKUP")
        info.SSHClient.backup(server, app)
    }
}