package tools

import node.Node
import java.io.File
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class DiffCache(val path: File, val changeCommand: String) {

    private val lock: Lock = ReentrantLock()

    init {
        if (!path.isDirectory) path.mkdir()
    }

    private fun getCacheFile(nodename: String, env: String): File {
        val cacheEnvDir = path.resolve(env)
        if (!cacheEnvDir.isDirectory) cacheEnvDir.mkdir()
        val cacheFile = cacheEnvDir.resolve(nodename)
        if (!cacheFile.isFile) cacheFile.createNewFile()
        return cacheFile
    }

    fun isChange(node: Node, env: String): Boolean {
        return lock.withLock { getCacheFile(node.name, env).readText() != getCacheInfo(node) }
    }

    fun updateCache(node: Node, env: String) {
        lock.withLock { getCacheFile(node.name, env).writeText(getCacheInfo(node)) }
    }

    private fun getCacheInfo(node: Node) = node.name + ":" + Commander().of(changeCommand).param("URL", node.remote).onDir(node.dir).run().output
}
