package commands

fun getEnv(server: String) = when (server) {
    "des01", "des02", "des03" -> "dev"
    "pre01", "pre02", "pre03" -> "pre"
    "stg" -> "stg"
    else -> throw NotImplementedError()
}

fun getUrl(server: String) = when (server) {
    "des01", "des02", "des03" -> "des.aireuropa.com"
    "pre01", "pre02", "pre03" -> "pre.aireuropa.com"
    "stg" -> "stg.aireuropa.com"
    else -> throw NotImplementedError()
}

fun getConfigFolder(env: String) = when (env) {
    "dev" -> "properties_desarrollo"
    "pre" -> "properties_preproduccion"
    "stg" -> "properties_produccion"
    else -> throw NotImplementedError()
}
