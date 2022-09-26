package tool

import java.io.File
import kotlin.system.exitProcess

val EXPR = "Expression"
typealias StringConsumer = (String) -> Unit

fun main(args: Array<String>) {
    if (args.size != 1) {
        System.err.println("Usage: generate_ast <output directory")
        exitProcess(64)
    }
    val outputDir = args.first()
    defineAst(
        outputDir, EXPR, listOf(
            "Binary   -> left: Expression, operator: Token , right: Expression",
            "Grouping -> expression: Expression",
            "Literal  -> value: Any",
            "Unary    -> operator: Token, right: Expression"
        )
    )
}

fun defineAst(outputDir: String, baseName: String, types: List<String>) {
    File("$outputDir/$baseName.kt").printWriter().use { out ->
        val write = { str: String -> out.println(str) }

        write("package klox")
        write("")
        write("abstract class $baseName()")
        types.forEach { type ->
            val split = type.split("->").map { it.trim() }
            val className = split[0]
            val fields = split[1]
            defineType(write, baseName, className, fields)
        }
    }
}

fun defineType(
    writer: StringConsumer,
    baseName: String,
    className: String,
    fieldList: String
) {
    val fields = fieldList.split(",")
        .map { it.trim() }
        .map { "val $it,\n" }
        .foldRight("") { s: String, acc: String -> "$acc$s" }

    writer("data class $className($fields): $baseName()")

}