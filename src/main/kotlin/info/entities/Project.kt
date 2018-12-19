package info.entities

data class Project(
    val name: String,
    val url: String,
    val target: String,
    val deps: List<String>
)
