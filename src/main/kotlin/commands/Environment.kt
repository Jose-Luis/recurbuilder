package commands

fun getEnv(server: String) = when (server) {
    "des01", "des02", "des03" -> "dev"
    "pre01", "pre02", "pre03" -> "pre"
    else -> throw NotImplementedError()
}

fun getServerPrefix(server: String) = when (server) {
    "des01", "des02", "des03" -> "des"
    "pre01", "pre02", "pre03" -> "pre"
    else -> throw NotImplementedError()
}

fun getUrl(server: String) = when (server) {
    "des01", "des02", "des03" -> "des.aireuropa.com"
    "pre01", "pre02", "pre03" -> "pre.aireuropa.com"
    else -> throw NotImplementedError()
}
