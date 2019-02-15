package commands

import info.Info
import node.visitors.*
import node.visitors.modifiers.Composed
import node.visitors.modifiers.Downloaded
import node.visitors.modifiers.OnlyIf
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
    private val builder = Downloaded(
        info,
        Composed(
            Printer(),
            RepoStatusPrinter(info),
            Updater(info),
            LocalChangeChecker(info, env),
            if (force) MavenBuilder(info, skipTests, env) else OnlyIf("dirty", MavenBuilder(info, skipTests, env)),
            LocalCacheUpdater(info, env)
        )
    )

    fun build() {
        when (mode) {
            "deps" -> info.projects.visitNode(nodename, PreOrder(builder))
            "cascade" -> info.projects.visit({ node -> node.dependsOn(nodename) }, PreOrder(builder))
            "alone" -> info.projects.visitNode(nodename, builder)
            else -> throw NotImplementedException()
        }
    }
}