package node

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Node(val name: String, val url: String) {
    var dirty: Boolean = false
    var cleaned: Boolean = false
    var parents: List<Node> = emptyList()

    private val lock: Lock = ReentrantLock()

    fun acceptVisitors(vararg visitors: NodeVisitor) {
        parents.parallelStream().forEach { it.acceptVisitors(*visitors) }
        lock.withLock { visitors.forEach { it.visit(this) } }
    }

    fun isDirty(): Boolean = lock.withLock { return !cleaned && (dirty || parents.any { it.isDirty() }) }
}
