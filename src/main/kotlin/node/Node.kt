package node

import info.Info
import info.entities.Project
import info.entities.Root
import node.visitors.NodeVisitor
import java.io.File
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Node(project: Project) {

    private var flags: MutableMap<String, Boolean> = mutableMapOf()

    var deps: List<Node> = emptyList()

    val name = project.name
    val remote = project.remote
    val dir = File(project.url)
    val target = dir.resolve(project.target)

    fun isDownloaded(workspace: File) = workspace.resolve(dir).exists()

    private val lock: Lock = ReentrantLock()

    fun acceptVisitor(visitor: NodeVisitor) {
        lock.withLock { visitor.visit(this) }
    }

    fun dependsOn(nodename: String): Boolean {
        return name == nodename || deps.any { dep -> dep.dependsOn(nodename) }
    }

    fun setFlag(property: String, value: Boolean) {
        flags.put(property, value)
    }

    fun unflag(property: String) {
        setFlag(property, false)
    }

    fun flag(property: String) {
        setFlag(property, true)
    }

    fun hasFlag(property: String): Boolean {
        return flags.getOrDefault(property, false)
    }

    fun hasOrInheritFlag(property: String): Boolean {
        return hasFlag(property) || deps.any { it.hasOrInheritFlag(property) }
    }
}
