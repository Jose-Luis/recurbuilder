import node.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    val projects = getProjectTree(args[0])

    projects[args[1]]?.acceptVisitors(
        UpdaterVisitor(),
        ChangeCheckerVisitor(),
        DirtyPrinterVisitor(),
        DirtyBuilderVisitor()
    )
    exitProcess(0)
}

fun getProjectTree(basedir: String): Map<String, Node> {
    var projects = mapOf(
        toPair("availability", basedir.plus("aireuropa-availability-parent")),
        toPair("service", basedir.plus("aireuropa-service-parent")),
        toPair("checkin", basedir.plus("aireuropa-checkin-parent")),
        toPair("backoffice", basedir.plus("aireuropa-backoffice-parent")),
        toPair("availability-provider", basedir.plus("aireuropa-availability-provider-parent")),
        toPair("payment", basedir.plus("aireuropa-payment-parent")),
        toPair("commons", basedir.plus("aireuropa-commons-parent")),
        toPair("ticketing", basedir.plus("aireuropa-ticketing-parent")),
        toPair("amadeus", basedir.plus("aireuropa-amadeus-parent")),
        toPair("connam", basedir.plus("aireuropa-connam-parent")),
        toPair("dreamliner", basedir.plus("dreamliner-commons-parent"))
    )
    "availability-provider".setParents(projects, "commons")
    "availability".setParents(projects, "availability-provider")
    "service".setParents(projects, "availability-provider", "payment")
    "checking".setParents(projects, "payment")
    "backoffice".setParents(projects, "payment")
    "payment".setParents(projects, "commons", "ticketing")
    "ticketing".setParents(projects, "amadeus")
    "amadeus".setParents(projects, "connam")
    "connam".setParents(projects, "dreamliner")
    "commons".setParents(projects, "dreamliner")

    return projects
}

fun toPair(name: String, url: String): Pair<String, Node> = Pair(name, Node(name, url))

fun String.setParents(projects: Map<String, Node>, vararg parents: String) {
    projects[this]?.parents = parents.filter { projects.containsKey(it) }.map { projects.getValue(it) }
}
