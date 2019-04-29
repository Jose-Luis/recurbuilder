package info

import info.entities.Root
import node.Node
import node.visitors.NodeVisitor

class ProjectTree(rootInfo: Root) {
    operator fun get(nodename: String?): Node {
        return projects[nodename]!!
    }

    private val projects: Map<String, Node> = rootInfo.projects.map { Node(it) }.associateBy { it.name }

    init {
        rootInfo.projects.forEach { projects[it.name]!!.deps = it.deps.map(projects::getValue) }
    }

    fun all(): Collection<Node> = projects.values

    fun serialVisit(condition: (Node) -> Boolean): Visitable {
        return object : Visitable {
            override fun with(visitor: NodeVisitor) = projects.values.filter { condition.invoke(it) }
                .forEach { node -> node.acceptVisitor(visitor) }
        }
    }

    fun visit(condition: (Node) -> Boolean): Visitable {
        return object : Visitable {
            override fun with(visitor: NodeVisitor) = projects.values.filter { condition.invoke(it) }
                .parallelStream()
                .forEach { node -> node.acceptVisitor(visitor) }
        }
    }

    interface Visitable {
        fun with(visitor: NodeVisitor)
    }
}
