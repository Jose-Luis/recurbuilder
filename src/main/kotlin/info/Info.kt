package info

import com.beust.klaxon.Klaxon
import info.entities.*
import tools.SSHClient
import java.io.File

class Info(file: File, workspace: File?) {

    private val root: Root

    val projects: ProjectTree
    val cacheDir: File
    val propertiesRepo: String
    val workspace: File
    val proxyConnection: String
    val commands: Commands
    val SSHClient: SSHClient
    val servers: Map<String, Server>
    val apps: Map<String, App>
    val proxies: Map<String, Proxy>

    init {
        root = Klaxon().parse<Root>(file.inputStream())!!
        projects = ProjectTree(root)
        commands = root.commands
        this.workspace = workspace ?: File(root.`workspace-dir`)
        proxyConnection = root.`proxy-connection`
        servers = root.servers.associateBy { it.name }
        apps = root.apps.associateBy { it.name }
        proxies = root.proxies.associateBy { it.name }
        SSHClient = SSHClient(root.`backup-dir`)
        cacheDir = File(root.`cache-dir`).absoluteFile
        propertiesRepo = root.`properties-repo`
    }
}
