package info.entities

data class Root(
    val `repos-dir`: String,
    val `cache-dir`: String,
    val `backup-dir`: String,
    val commands: Commands,
    val projects: List<Project>,
    val servers: List<Server>,
    val apps: List<App>
)
