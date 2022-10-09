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
        val tokens = scanner.scanTokens()

        val parser = Parser(tokens)
        val expression = parser.parse()

        if (hadError) {
            return
        }
        println(expression?.let { AstPrinter().print(it) })

    }

    companion object {
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
    }

}
