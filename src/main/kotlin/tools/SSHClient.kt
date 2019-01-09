package tools

import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import info.entities.App
import info.entities.Server
import node.Node
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter

class SSHClient(val `backup-dir`: String) {

    fun backup(server: Server, app: App) {
        doInSftpChannel(server, action = { channel ->
            channel.get(server.`webapp-dir`.plus("/").plus(app.dir).plus("/").plus(app.file)).use { input ->
                val dir = File(`backup-dir`).absoluteFile
                if (!dir.isDirectory) dir.mkdir()
                val serverDir = dir.resolve(server.name).absoluteFile
                if (!serverDir.isDirectory) serverDir.mkdir()
                val nowDir = serverDir.resolve(DateTimeFormatter.ofPattern("YYYYMMddHHmmss").format(now())).absoluteFile
                if (!nowDir.isDirectory) nowDir.mkdir()
                val file = nowDir.resolve(app.file).absoluteFile
                if (!file.isFile) file.createNewFile()
                file.outputStream().use { input.copyTo(it) }
            }
        })
    }

    fun waitUntilUndeploy(server: Server, app: App): Boolean {
        var clear = false
        doInSftpChannel(server, action = { channel ->
            clear = channel.ls(server.`webapp-dir`.plus("/").plus(app.dir)).size == 0
            if (!clear) {
                for (i in 0..3) {
                    Thread.sleep(5000)
                    clear = channel.ls(server.`webapp-dir`.plus("/").plus(app.dir)).size == 0
                    if (clear) break;
                }
            }
        })
        return clear
    }

    fun delete(server: Server, app: App) {
        doInSftpChannel(server, action = { channel ->
            channel.rm(server.`webapp-dir`.plus("/").plus(app.dir).plus("/").plus(app.file))
        })
    }

    fun upload(server: Server, app: App, project: Node) {
        doInSftpChannel(server, action = { channel ->
            channel.put(
                project.target.inputStream(),
                server.`webapp-dir`.plus("/").plus(app.dir).plus("/").plus(app.file)
            )
        })
    }

    fun showLog(server: Server, app: App) {
        val session = createSession(server)
        session.connect(15000)
        session.serverAliveInterval = 15000
        val channel = session.openChannel("exec") as ChannelExec
        try {
            val now = now()
            val filePattern = "tomcat%s/logs/catalina-%s-%02d-%02d.out"
            val logfile = String.format(filePattern, app.port, now.year, now.month.value, now.dayOfMonth)
            channel.setCommand("tail -300f ".plus(server.`log-dir`).plus(logfile))
            channel.setPty(true)
            channel.connect()
            val bufferedReader = BufferedReader(InputStreamReader(channel.inputStream));
            while (true) {
                if (bufferedReader.ready()) {
                    val line = bufferedReader.readLine();
                    System.out.println(line);
                }
            }
        } finally {
            channel.sendSignal("SIGINT");
            channel.disconnect();
            session.disconnect();
            System.out.println("exit");
        }
    }

    private fun createSession(server: Server): Session {
        val jsch = JSch()
        val session = jsch.getSession(server.user, server.ip, 22)
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(server.password)
        return session
    }

    private fun doInSftpChannel(server: Server, action: (ChannelSftp) -> Unit) {
        val session = createSession(server)
        var channel: ChannelSftp? = null
        try {
            session.connect()
            channel = session.openChannel("sftp") as ChannelSftp
            channel.connect()
            action.invoke(channel)
        } finally {
            channel?.disconnect()
            channel?.exit()
            session.disconnect()
        }
    }
}