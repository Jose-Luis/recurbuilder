package tools

import java.io.File


class Commander() {
    private lateinit var commandString: String
    private lateinit var workingDir: File
    private lateinit var output: File
    private var params: MutableMap<String, String> = HashMap()
    private var verbose = false
    private var showCommand = false

    fun onDir(workingDir: File): Commander {
        this.workingDir = workingDir
        return this
    }

    fun showCommand(showCommand: Boolean): Commander {
        this.showCommand = showCommand
        return this
    }

    fun output(outputFile: File): Commander {
        this.output = outputFile
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
        if (showCommand) println("--> Executing command: $command")
        return command.split(" ").toTypedArray()
    }

    fun start() = ProcessBuilder(*resolveCommandString()).directory(workingDir).start()

    fun startIO() {
        ProcessBuilder(*resolveCommandString()).directory(workingDir).inheritIO().start().waitFor()
    }

    fun start(output: File) =
        ProcessBuilder(*resolveCommandString()).inheritIO().directory(workingDir)
            .redirectError(output).redirectOutput(output).start()

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

