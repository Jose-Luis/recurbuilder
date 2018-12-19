package info

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
        val session = createSession(server)
        session.connect()
        val channel: ChannelSftp = session.openChannel("sftp") as ChannelSftp
        channel.connect()
        channel.get(server.`webapp-dir`.plus("/").plus(app.dir).plus("/").plus(app.file)).use { input ->
            val dir = File(`backup-dir`).absoluteFile
            if (!dir.isDirectory) dir.mkdir()
            val serverDir = dir.resolve(server.name)
            if (!serverDir.isDirectory) serverDir.mkdir()
            val file =
                serverDir.resolve(DateTimeFormatter.ofPattern("YYYYMMddHHmmss").format(now()).plus("-").plus(app.file))
                    .absoluteFile
            if (!file.isFile) file.createNewFile()
            file.outputStream().use { input.copyTo(it) }
        }
        channel.exit()
        session.disconnect()
    }

    fun waitUntilUndeploy(server: Server, app: App): Boolean {
        val session = createSession(server)
        session.connect()
        val channel: ChannelSftp = session.openChannel("sftp") as ChannelSftp
        channel.connect()
        var clean = channel.ls(server.`webapp-dir`.plus("/").plus(app.dir)).size == 0
        if (!clean) {
            for (i in 0..3) {
                Thread.sleep(5000)
                clean = channel.ls(server.`webapp-dir`.plus("/").plus(app.dir)).size == 0
                if (clean) break;
            }
        }
        return clean
    }

    fun delete(server: Server, app: App) {
        val session = createSession(server)
        session.connect()
        val channel: ChannelSftp = session.openChannel("sftp") as ChannelSftp
        channel.connect()
        channel.rm(server.`webapp-dir`.plus("/").plus(app.dir).plus("/").plus(app.file))
        channel.exit()
        session.disconnect()
    }

    fun upload(server: Server, app: App, project: Node) {
        val session = createSession(server)
        session.connect()
        val channel: ChannelSftp = session.openChannel("sftp") as ChannelSftp
        channel.connect()
        channel.put(project.target.inputStream(), server.`webapp-dir`.plus("/").plus(app.dir).plus("/").plus(app.file))
        channel.exit()
        session.disconnect()
    }

    fun showLog(server: Server, app: App) {
        val session = createSession(server)
        session.connect(15000)
        session.serverAliveInterval = 15000
        val channel = session.openChannel("exec") as ChannelExec
        val now = now()
        val logfile =
            String.format("tomcat%s/logs/catalina-%s-%s-%s.out", app.port, now.year, now.month.value, now.dayOfMonth)
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
        bufferedReader.close();
        channel.sendSignal("SIGINT");
        channel.disconnect();
        session.disconnect();
        System.out.println("exit");
    }

    private fun createSession(server: Server): Session {
        val jsch = JSch()
        val session = jsch.getSession(server.user, server.ip, 22)
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(server.password)
        return session
    }
}