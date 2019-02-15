package tools

import info.Info
import node.Node

class LocalCacheReader(val node: Node, info: Info) : CacheReader {
    val command = Commander().of(info.commands.changes).onDir(info.workspace.resolve(node.dir))

    override fun readCache(): String {
        return "${node.name} -> ${command.run().output}"
    }
}