package tools

import info.Info
import node.Node

class RemoteCacheReader(val node: Node, branch: String, info: Info) : CacheReader {
    val command = Commander().of(info.commands.`remote-changes`).onDir(info.workspace)
        .param("URL", node.remote)
        .param("BRANCH", branch)

    override fun readCache(): String {
        return "${node.name} -> ${command.run().output}"
    }
}