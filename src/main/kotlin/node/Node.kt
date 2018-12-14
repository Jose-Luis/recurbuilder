package node

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Node(val name: String, val url: String) {

    private var flags: MutableMap<String, Boolean> = mutableMapOf()

    var deps: List<Node> = emptyList()

    private val lock: Lock = ReentrantLock()

    fun acceptVisitorAfterDeps(visitor: NodeVisitor) {
        deps.parallelStream().forEach { it.acceptVisitorAfterDeps(visitor) }
        acceptVisitor(visitor)
    }

    fun acceptVisitor(visitor: NodeVisitor) {
        lock.withLock { visitor.visit(this) }
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
