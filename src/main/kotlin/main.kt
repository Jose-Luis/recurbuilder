import cli.*
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) = Tool().subcommands(
    Build(),
    Builploy(),
    Changes(),
    LocalDeploy(),
    Download(),
    Execute(),
    NexusTreeDeploy(),
    RemoteDeploy(),
    CloneAll(),
    Clone(),
    Update(),
    ServiceStarter(),
    SetVersion(),
    SetDependencyVersion(),
    Log()
).main(args)


