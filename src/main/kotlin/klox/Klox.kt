package klox

import java.io.File

fun main(args: Array<String>) {
    println("running")
    val klox = Klox()
    if (args.size > 1) {
        print("Usage: klox [script]")
    } else if (args.size == 1) {
        klox.runFile(args[0])
    } else {
        klox.runPrompt()
    }
}

class Klox {
    private var hadError = false
    private val interpreter = Interpreter()

    fun runFile(path: String) {
        run(File(path).readText(Charsets.UTF_8))
        if (hadError) {
            System.exit(65)
        }
        if (hadRuntimeError) {
            System.exit(70)
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
        val tokens = scanner.scanTokens()

        val parser = Parser(tokens)
        val statements = parser.parse()

        if (hadError) {
            return
        }
        interpreter.interpret(statements)
    }

    companion object {
        private var hadRuntimeError = false
        private var hadError = false
        fun error(lineNum: Int, message: String) {
            report(lineNum, "", message)
        }

        fun error(token: Token, message: String) {
            if (token.type == TokenType.EOF) {
                report(token.line, " at end", message)
            } else {
                report(token.line, " at '${token.lexeme}'", message)
            }
        }

        private fun report(lineNum: Int, where: String, message: String) {
            System.err.println("[line $lineNum] Error $where: $message")
            hadError = true
        }

        fun runtimeError(ex: RuntimeError) {
            System.err.println("$ex.message\n[line ${ex.token.line}]")
            hadRuntimeError = true
        }
    }

}
