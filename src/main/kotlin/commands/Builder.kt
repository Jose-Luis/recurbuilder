package commands

import info.Info
import node.Node
import node.visitors.*
import node.visitors.modifiers.Composed
import node.visitors.modifiers.Downloaded
import node.visitors.modifiers.OnlyIf
import node.visitors.modifiers.Once
import node.visitors.modifiers.PreOrder
import sun.reflect.generics.reflectiveObjects.NotImplementedException

class Builder
    (
    private val nodename: String,
    private val env: String,
    private val skipTests: Boolean,
    private val force: Boolean,
    private val mode: String,
    val info: Info
) {
    private val projectBuilder = Once(
        Composed(
        Printer(),
        RepoStatusPrinter(info),
        Updater(info),
        LocalChangeChecker(info, env),
        if (force) MavenBuilder(info, skipTests, env) else OnlyIf("dirty", MavenBuilder(info, skipTests, env)),
        LocalCacheUpdater(info, env)
    ))

    private fun buildInPreOrder(node: Node) = node.acceptVisitor(PreOrder(Downloaded(info, projectBuilder)))
    private fun buildAlone(node: Node) = node.acceptVisitor(Downloaded(info, projectBuilder))

    fun build() {
        val projects = info.projects.all()
        when (mode) {
            "deps" -> projects.filter { it.name == nodename }.parallelStream().forEach { buildInPreOrder(it) }
            "cascade" -> projects.filter { it.dependsOn(nodename) }.parallelStream().forEach { buildInPreOrder(it) }
            "alone" -> projects.filter { it.name == nodename }.parallelStream().forEach { buildAlone(it) }
            else -> throw NotImplementedException()
        }
    }
}