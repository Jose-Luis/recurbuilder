package tools

import node.Node
import java.io.File
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class DiffCache(path: File, subfolder: String, val changeCommand: String) {

    private val lock = ReentrantLock()
    private val folder: File

    init {
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
        return lock.withLock { getCacheFile(node.name, branch, env).readText() != getCacheInfo(node, branch) }
    }

    fun updateCache(node: Node, branch: String, env: String) {
        lock.withLock { getCacheFile(node.name, branch, env).writeText(getCacheInfo(node, branch)) }
    }

    private fun getCacheInfo(node: Node, branch: String) = node.name + ":" +
            Commander().of(changeCommand).onDir(folder)
                .param("URL", node.remote)
                .param("BRANCH", branch)
                .run().output
}
