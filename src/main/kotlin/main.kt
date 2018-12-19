import cli.*
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) = Tool().subcommands(
    Build(),
    Changes(),
    Deploy(),
    Log()
).main(args)


