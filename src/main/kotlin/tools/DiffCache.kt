package tools

import info.Info
import node.Node
import java.io.File
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class DiffCache(val info: Info, subfolder: String, val cacheReader: CacheReader) {

    private val lock = ReentrantLock()
    private val folder: File

    init {
        val path = info.cacheDir
        if (!path.isDirectory) path.mkdir()
        folder = path.resolve(subfolder)
        if (!folder.isDirectory) folder.mkdir()
    }

    private fun getCacheFile(nodename: String, branch: String, env: String): File {
        val cacheBranchDir = folder.resolve(branch)
        if (!cacheBranchDir.isDirectory) cacheBranchDir.mkdir()
        val cacheEnvDir = cacheBranchDir.resolve(env)
        if (!cacheEnvDir.isDirectory) cacheEnvDir.mkdir()
        val cacheFile = cacheEnvDir.resolve(nodename)
        if (!cacheFile.isFile) cacheFile.createNewFile()
        return cacheFile
    }

    fun isChange(node: Node, branch: String, env: String): Boolean {
        return lock.withLock { getCacheFile(node.name, branch, env).readText() != cacheReader.readCache() }
    }

    fun updateCache(node: Node, branch: String, env: String) {
        lock.withLock { getCacheFile(node.name, branch, env).writeText(cacheReader.readCache()) }
    }
}

