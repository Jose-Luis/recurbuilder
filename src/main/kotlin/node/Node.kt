package node

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Node(val name: String, val url: String, val repoUrl: String) {
    var dirty: Boolean = false
    var treated: Boolean = false
    var parents: List<Node> = emptyList()

    private val lock: Lock = ReentrantLock()

    fun acceptVisitorInPostOrder(visitors: NodeVisitor) {
        parents.parallelStream().forEach { it.acceptVisitorInPostOrder(visitors) }
        lock.withLock { visitors.visit(this) }
    }

    fun isRoot(): Boolean = parents.isEmpty()

    fun isDirty(): Boolean = lock.withLock { return !treated && (dirty || parents.any { it.isDirty() }) }
}
