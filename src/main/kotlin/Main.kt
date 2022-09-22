import java.io.File
import java.util.Scanner

fun main(args: Array<String>) {
    println("running")
    var lox = Lox()
    if (args.size > 1) {
        print("Usage: klox [script]")
    } else if (args.size == 1) {
        lox.runFile(args[0])
    } else {
        lox.runPrompt()
    }
}

class Lox {
    private var hadError = false

    fun runFile(path: String) {
        run(File(path).readText(Charsets.UTF_8))
        if (hadError) {
            System.exit(65)
        }
    }

    fun runPrompt() {
        while (true) {
            print("> ")
            val line = readLine()
            if (line == null) {
                println("Line was null")
                break
            }
            run(line)
            hadError = false
        }
    }

    fun run(text: String) {
        val scanner = Scanner(text)
        val tokens = scanner.tokens().toList()
        tokens.map { println(it) }
    }

    companion object {
        private var hadError = false
        fun error(lineNum: Int, message: String) {
            report(lineNum, "", message)
        }

        private fun report(lineNum: Int, where: String, message: String) {
            System.err.println("[line $lineNum] Error $where: $message")
            hadError = true;
        }
    }

}
