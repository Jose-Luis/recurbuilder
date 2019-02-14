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

    fun run(): CommandResult {
        params.entries.forEach { commandString = commandString.replace("$".plus(it.key), it.value) }
        val command = commandString.trim().replace("\\s+".toRegex(), " ")
        val processBuilder = ProcessBuilder(*command.split(" ").toTypedArray())
            .directory(workingDir)
            .inheritIO()
        val output = File.createTempFile("stdout", "txt")
        val error = File.createTempFile("stderr", "txt")
        if (!verbose) {
            processBuilder.redirectOutput(output)
            processBuilder.redirectError(error)
        }
        val process = processBuilder.start()
        process.waitFor()
        return CommandResult(output.readText(), error.readText(), process.exitValue())
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

