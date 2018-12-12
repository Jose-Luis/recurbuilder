import java.io.File
import java.util.function.Consumer
import kotlin.system.exitProcess

private const val GIT_STATUS = "git status --porcelain"
private const val GIT_PULL = "git pull origin development"
private const val CHANGES_COMMAND = GIT_STATUS;
private const val MVN = "mvn -T 4 clean install -PskipSiebelTests"

class Treater(private val basedir: String) : Consumer<Node> {
    override fun accept(node: Node) {
        val file = File(basedir.plus(node.url));
        System.out.println("****** TREATING ${node.name.toUpperCase()} ******")
        node.changes = !CHANGES_COMMAND.runCommand(file).output().isBlank();
        GIT_STATUS.runCommandToIO(file)
        GIT_PULL.runCommandToIO(file)
        if (node.mustBeTreated()) {
            if (MVN.runCommandToIO(file).exitValue() == 0) {
                node.treated = true
            } else {
                exitProcess(33);
            }
        }
    }
}

fun String.runCommandToIO(workingDir: File): Process {
    val parts = "cmd /c \"${this}\"".split("\\s".toRegex())
    val proc = ProcessBuilder(*parts.toTypedArray())
        .directory(workingDir)
        .inheritIO()
        .start()
    proc.waitFor()
    return proc
}

fun String.runCommand(workingDir: File): Process {
    val parts = "cmd /c \"${this}\"".split("\\s".toRegex())
    val proc = ProcessBuilder(*parts.toTypedArray())
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()
    proc.waitFor()
    return proc
}

fun Process.output(): String {
    return this.inputStream.bufferedReader().readText()
}
