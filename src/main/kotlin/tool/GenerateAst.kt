package tool

import java.io.File
import java.util.*
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
        write("abstract class $baseName() {")
        defineVisitor(write, baseName, types)
        write("abstract fun <R> accept(visitor: Visitor<R>): R")
        write("}")
        types.forEach { type ->
            val split = type.split("->").map { it.trim() }
            val className = split[0]
            val fields = split[1]
            defineType(write, baseName, className, fields)
        }
    }
}

fun defineVisitor(write: StringConsumer, baseName: String, types: List<String>) {
    write("interface Visitor<R> {")
    types.forEach { type ->
        val typeName = type.split("->").first().trim()
        write("fun visit$typeName$baseName(${baseName.lowercase(Locale.getDefault())}: $typeName): R")
    }
    write("}")
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
        .reversed()
        .fold("") { s: String, acc: String -> "$acc$s" }

    writer("class $className($fields): $baseName() {")
    writer("override fun <R> accept(visitor: Visitor<R>): R { return visitor.visit$className$baseName(this)}")
    writer("}")

}