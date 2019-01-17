package tools

import java.io.File


class Commander() {
    private lateinit var commandString: String
    private lateinit var workingDir: File
    private var verbose = false

    fun onDir(workingDir: File): Commander {
        this.workingDir = workingDir
        return this
    }

    fun of(commandString: String): Commander {
        this.commandString = when (OS()) {
            "windows" -> "cmd /c \"$commandString\""
            "unix", "linux" -> commandString
            else -> throw NotImplementedError()
        }
        this.commandString.trim()
        return this
    }

    fun verbose(verbose: Boolean): Commander {
        this.verbose = verbose
        return this
    }

    fun run(): CommandResult {
        val processBuilder = ProcessBuilder(*commandString.split(" ").toTypedArray())
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

    class CommandResult(val output: String, val error: String, val exitCode: Int) {
        fun isOk() = exitCode == 0
    }
}

