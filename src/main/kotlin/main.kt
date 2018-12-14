import cli.builder.Builder
import cli.builder.Dirty
import cli.builder.Forced
import com.beust.klaxon.Klaxon
import com.github.ajalt.clikt.core.subcommands
import node.Node
import node.StatusCache
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val cache = StatusCache("buildercache")
    Builder().subcommands(Dirty(cache), Forced(cache)).main(args)
    exitProcess(0)
}


