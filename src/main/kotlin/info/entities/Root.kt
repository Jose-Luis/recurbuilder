package info.entities

data class Root(
    val `workspace-dir`: String,
    val `cache-dir`: String,
    val `backup-dir`: String,
    val `proxy-connection`: String,
    val commands: Commands,
    val projects: List<Project>,
    val servers: List<Server>,
    val apps: List<App>,
    val proxies: List<Proxy>
)
