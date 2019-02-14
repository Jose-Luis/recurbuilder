package info.entities

data class Commands(
    val build: String,
    val update: String,
    val print: String,
    val changes: String,
    val `remote-changes`: String,
    val `nexus-deploy`: String,
    val clone: String

)
