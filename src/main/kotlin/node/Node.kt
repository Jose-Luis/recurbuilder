package node

import node.visitors.NodeVisitor
import java.io.File
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Node(val name: String, val dir: File) {

    private var flags: MutableMap<String, Boolean> = mutableMapOf()

    var deps: List<Node> = emptyList()

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
