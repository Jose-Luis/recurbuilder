@FunctionalInterface
public interface Visitor {
    void visit(Node node);

    default Visitor andThen(Visitor other) {
        return node -> {
            this.visit(node);
            other.visit(node);
        };
    }
}
