import java.io.File
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class Treater(val basedir: String) : Consumer<Node> {
    override fun accept(t: Node) {
        val file = File(basedir.plus(t.name));
        val gitOutput = "git status --porcelain".runCommand(file).output()
        if (!gitOutput.isBlank()) {
            System.out.println("====== COMPILING ${t.name}")
            val mvnExec = "mvn clean install -DskipTests".runCommand(file)
            if (mvnExec.exitValue() == 0) {
                t.setTreated(true)
            } else {
                System.out.println(mvnExec.output())
                System.out.println(mvnExec.error())
            }
        }
    }
}

fun String.runCommand(workingDir: File): Process {
    val parts = this.split("\\s".toRegex())
    val proc = ProcessBuilder(*parts.toTypedArray())
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()
    proc.waitFor(60, TimeUnit.MINUTES)
    return proc
}

fun Process.output(): String {
    return this.inputStream.bufferedReader().readText()
}

fun Process.error(): String {
    return this.errorStream.bufferedReader().readText()
}
