import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node {
    private final String name;
    private final String url;
    private boolean dirty;
    private boolean cleaned;
    private List<Node> parents;
    private Lock lock;

    public Node(String name, String url) {
        this.name = name;
        this.url = url;
        this.lock = new ReentrantLock();
        this.dirty = false;
        this.cleaned = false;
    }

    public void accept(Visitor visitor) {
        parents.parallelStream().forEach(node -> node.accept(visitor));
        lock.lock();
        visitor.visit(this);
        lock.unlock();
    }

    public boolean isDirty() {
        lock.lock();
        boolean isDirty = !cleaned && (dirty || parents.stream().anyMatch(Node::isDirty));
        lock.unlock();
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isCleaned() {
        return cleaned;
    }

    public void setCleaned(boolean cleaned) {
        this.cleaned = cleaned;
    }

    public List<Node> getParents() {
        return parents;
    }

    public void setParents(List<Node> parents) {
        this.parents = parents;
    }
}

