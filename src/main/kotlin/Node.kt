import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import java.util.function.Consumer
import kotlin.concurrent.withLock

class Node(val name: String, val url: String) {
    var changes: Boolean = false
    var treated: Boolean = false
    var parents: List<Node> = emptyList()

    private val lock: Lock = ReentrantLock()

    fun treat(action: Consumer<Node>) {
        parents.parallelStream().forEach { it.treat(action) }
        if (mustBeTreated()) {
            lock.withLock { action.accept(this) }
        }
    }

    fun mustBeTreated(): Boolean =
        lock.withLock { return !treated && (changes || parents.any { it.treated } || parents.isEmpty()) }
}
