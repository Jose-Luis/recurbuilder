package tools

import java.io.File


class Commander() {
    private lateinit var commandString: String
    private lateinit var workingDir: File
    private var params: MutableMap<String, String> = HashMap()
    private var verbose = false

    fun onDir(workingDir: File): Commander {
        this.workingDir = workingDir
        return this
    }

    fun of(commandString: String): Commander {
        this.commandString = when (OS()) {
            "unix", "linux" -> commandString.trim()
            else -> "cmd /c \"${commandString.trim()}\""
        }
        return this
    }

    fun verbose(verbose: Boolean): Commander {
        this.verbose = verbose
        return this
    }

    private fun resolveCommandString(): Array<String> {
        params.entries.forEach { commandString = commandString.replace("$".plus(it.key), it.value) }
        val command = commandString.trim().replace("\\s+".toRegex(), " ")
        return command.split(" ").toTypedArray()
    }

    fun start() {
        ProcessBuilder(*resolveCommandString()).directory(workingDir).inheritIO().start()
    }

    fun run(): CommandResult {
        val processBuilder = ProcessBuilder(*resolveCommandString()).directory(workingDir).inheritIO()
        val output = File.createTempFile("stdout", "txt")
        val error = File.createTempFile("stderr", "txt")
        if (!verbose) {
            processBuilder.redirectOutput(output)
            processBuilder.redirectError(error)
        }
        val process = processBuilder.start()
        process.waitFor()
        return CommandResult(output.readText(Charsets.UTF_8), error.readText(Charsets.UTF_8), process.exitValue())
    }

    fun OS(): String = System.getProperty("os.name").toLowerCase()

    fun param(k: String, env: String): Commander {
        params[k] = env
        return this
    }

    class CommandResult(val output: String, val error: String, val exitCode: Int) {
        fun isOk() = exitCode == 0
    }
}

