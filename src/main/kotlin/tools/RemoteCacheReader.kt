package tools

import info.Info
import node.Node

class RemoteCacheReader(node: Node, branch: String, info: Info) : CacheReader {
    val command = Commander().of(info.commands.`remote-changes`).onDir(info.workspace)
        .param("URL", node.remote)
        .param("BRANCH", branch)

    override fun readCache(): String {
        return command.run().output
    }
}