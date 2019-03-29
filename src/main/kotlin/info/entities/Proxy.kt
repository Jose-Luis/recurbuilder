package info.entities

data class Proxy(val name: String, val active: Boolean, val servers: Map<String, ProxyServer>)