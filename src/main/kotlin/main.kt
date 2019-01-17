import cli.*
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) = Tool().subcommands(
    Build(),
    Builploy(),
    Changes(),
    Deploy(),
    Download(),
    Log()
).main(args)


