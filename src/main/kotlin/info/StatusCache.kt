package info

import node.Node
import node.output
import node.runCommand
import java.io.File
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class StatusCache(path: String, val changeCommand: String) {

    private val cacheDir: File = File(path).absoluteFile
    private val lock: Lock = ReentrantLock()

    init {
        if (!cacheDir.isDirectory) cacheDir.mkdir()
    }

    private fun getCacheFile(nodename: String, env: String): File {
        val cacheEnvDir = cacheDir.resolve(env)
        if (!cacheEnvDir.isDirectory) cacheEnvDir.mkdir()
        val cacheFile = cacheEnvDir.resolve(nodename)
        if (!cacheFile.isFile) cacheFile.createNewFile()
        return cacheFile
    }

    fun isChange(node: Node, env: String): Boolean {
        return lock.withLock { getCacheFile(node.name, env).readText() != getCacheInfo(node, env) }
    }

    fun updateCache(node: Node, env: String) {
        lock.withLock { getCacheFile(node.name, env).writeText(getCacheInfo(node, env)) }
    }

    private fun getCacheInfo(node: Node, env: String) = "diff: $env\n ${changeCommand.runCommand(node.dir).output()}"
}
