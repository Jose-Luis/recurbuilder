package info.entities

data class Server(
    val name: String,
    val ip: String,
    val `webapp-dir`: String,
    val `log-dir`: String,
    val user: String,
    val password: String
)