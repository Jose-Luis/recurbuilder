import kotlin.system.exitProcess

fun main(args: Array<String>) {
    var projects = mapOf(
        toPair("availability", "aireuropa-availability-parent"),
        toPair("service", "aireuropa-service-parent"),
        toPair("checkin", "aireuropa-checkin-parent"),
        toPair("backoffice", "aireuropa-backoffice-parent"),
        toPair("availability-provider", "aireuropa-availability-provider-parent"),
        toPair("payment", "aireuropa-payment-parent"),
        toPair("commons", "aireuropa-commons-parent"),
        toPair("ticketing", "aireuropa-ticketing-parent"),
        toPair("amadeus", "aireuropa-amadeus-parent"),
        toPair("connam", "aireuropa-connam-parent"),
        toPair("dreamliner", "dreamliner-commons-parent")
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

    projects[args[1]]?.treat(Treater(args[0]))
    exitProcess(0)
}

fun toPair(name: String, url: String): Pair<String, Node> = Pair(name, Node(name, url))

fun String.setParents(projects: Map<String, Node>, vararg parents: String) {
    projects[this]?.parents = parents.filter { projects.containsKey(it) }.map { projects.getValue(it) }
}