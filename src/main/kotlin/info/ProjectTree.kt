package info

import info.entities.Root
import node.Node
import node.visitors.NodeVisitor
import java.util.function.Predicate

class ProjectTree(rootInfo: Root) {
    operator fun get(nodename: String?): Node {
        return projects[nodename]!!
    }

    private val projects: Map<String, Node> = rootInfo.projects.map { Node(it) }.associateBy { it.name }

    init {
        rootInfo.projects.forEach { projects[it.name]!!.deps = it.deps.map(projects::getValue) }
    }

    fun all(): Collection<Node> = projects.values

    fun visit(condition: (Node) -> Boolean, visitor: NodeVisitor) {
        projects.values.filter { condition.invoke(it) }.parallelStream()
            .forEach { node -> node.acceptVisitor(visitor) }
    }

    fun visitNode(nodename: String, visitor: NodeVisitor) {
        visit({ node -> node.name == nodename }, visitor)
    }

}
