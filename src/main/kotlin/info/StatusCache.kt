package info

import node.Node
import node.output
import node.runCommand
import java.io.File

class StatusCache(path: String, val changeCommand: String) {

    private val cacheDir: File = File(path).absoluteFile

    init {
        if (!cacheDir.isDirectory) cacheDir.mkdir()
    }

    private fun getCacheFile(nodename: String): File {
        val cacheFile = cacheDir.resolve(nodename)
        if (!cacheFile.isFile) cacheFile.createNewFile()
        return cacheFile
    }

    fun isChange(node: Node): Boolean {
        return getCacheFile(node.name).readText() != getCacheInfo(node)
    }

    fun updateCache(node: Node) {
        getCacheFile(node.name).writeText(getCacheInfo(node))
    }

    private fun getCacheInfo(node: Node) = "diff: ".plus(changeCommand.runCommand(node.dir).output())
}
