package info.entities

data class Root(
    val `workspace-dir`: String,
    val `cache-dir`: String,
    val `backup-dir`: String,
    val commands: Commands,
    val projects: List<Project>,
    val servers: List<Server>,
    val apps: List<App>
)
