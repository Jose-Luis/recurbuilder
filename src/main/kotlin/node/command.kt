package node

import java.io.File

fun String.run(workingDir: String): Process {
    val proc = ProcessBuilder(*("cmd /c \"${this}\"".split("\\s".toRegex())).toTypedArray())
        .directory(File(workingDir))
        .inheritIO()
        .start()
    proc.waitFor()
    return proc
}

fun String.runCommand(workingDir: String): Process {
    val proc = ProcessBuilder(*("cmd /c \"${this}\"".split("\\s".toRegex())).toTypedArray())
        .directory(File(workingDir))
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()
    proc.waitFor()
    return proc
}

fun Process.output(): String {
    return this.inputStream.bufferedReader().readText()
}
