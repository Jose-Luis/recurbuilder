package tools

import java.io.File

fun print(file: File, color: String) {
    file.forEachLine { println("$color $it $ANSI_RESET") }
}

fun print(line: String, color: String) {
    println("$color $line $ANSI_RESET")
}

val ANSI_RESET = "\u001B[0m"
val ANSI_BLACK = "\u001B[30m"
val ANSI_RED = "\u001B[31m"
val ANSI_GREEN = "\u001B[32m"
val ANSI_YELLOW = "\u001B[33m"
val ANSI_BLUE = "\u001B[34m"
val ANSI_PURPLE = "\u001B[35m"
val ANSI_CYAN = "\u001B[36m"
val ANSI_WHITE = "\u001B[37m"
val ANSI_BRIGHT_BLACK = "\u001B[90m"
val ANSI_BRIGHT_RED = "\u001B[91m"
val ANSI_BRIGHT_GREEN = "\u001B[92m"
val ANSI_BRIGHT_YELLOW = "\u001B[93m"
val ANSI_BRIGHT_BLUE = "\u001B[94m"
val ANSI_BRIGHT_PURPLE = "\u001B[95m"
val ANSI_BRIGHT_CYAN = "\u001B[96m"
val ANSI_BRIGHT_WHITE = "\u001B[97m"

val ANSI_BG_BLACK = "\u001B[40m";
val ANSI_BG_RED = "\u001B[41m";
val ANSI_BG_GREEN = "\u001B[42m";
val ANSI_BG_YELLOW = "\u001B[43m";
val ANSI_BG_BLUE = "\u001B[44m";
val ANSI_BG_PURPLE = "\u001B[45m";
val ANSI_BG_CYAN = "\u001B[46m";
val ANSI_BG_WHITE = "\u001B[47m";
val ANSI_BRIGHT_BG_BLACK = "\u001B[100m";
val ANSI_BRIGHT_BG_RED = "\u001B[101m";
val ANSI_BRIGHT_BG_GREEN = "\u001B[102m";
val ANSI_BRIGHT_BG_YELLOW = "\u001B[103m";
val ANSI_BRIGHT_BG_BLUE = "\u001B[104m";
val ANSI_BRIGHT_BG_PURPLE = "\u001B[105m";
val ANSI_BRIGHT_BG_CYAN = "\u001B[106m";
val ANSI_BRIGHT_BG_WHITE = "\u001B[107m";
