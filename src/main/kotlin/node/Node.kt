package node

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Node(val name: String, val url: String) {
    var dirty: Boolean = false
    var treated: Boolean = false
    var parents: List<Node> = emptyList()

    private val lock: Lock = ReentrantLock()

    fun acceptVisitor(visitor: NodeVisitor) {
        parents.parallelStream().forEach { it.acceptVisitor(visitor) }
        lock.withLock { visitor.visit(this) }
    }

    fun acceptVisitors(vararg visitors: NodeVisitor) {
        visitors.forEach { acceptVisitor(it) }
    }

    fun isRoot(): Boolean = parents.isEmpty()

    fun isDirty(): Boolean = lock.withLock { return !treated && (dirty || parents.any { it.dirty }) }
}
