import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import java.util.function.Consumer
import kotlin.concurrent.withLock

class Node(val name: String) {
    private var parents: List<Node> = emptyList()
    private var treated: Boolean = false
    private val lock: Lock = ReentrantLock()

    fun setParents(parents: List<Node>) {
        this.parents = parents
    }

    fun setTreated(treated: Boolean) {
        this.treated = treated
    }

    fun isTreated(): Boolean = lock.withLock { return this.treated }

    fun treat(action: Consumer<Node>) {
        parents.parallelStream().forEach { it.treat(action) }
        if (!isTreated() && (parents.any { it.isTreated() } || parents.isEmpty())) {
            lock.withLock { action.accept(this) }
        }
    }
}
