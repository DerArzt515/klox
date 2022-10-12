package klox

import klox.TokenType.*

class Interpreter : Expr.Visitor<Any>, Stmt.Visitor<Unit> {
    private var environment = Environment()

    fun interpret(statements: List<Stmt>) {
        try {
            statements.forEach { execute(it) }
        } catch (ex: RuntimeError) {
            Klox.runtimeError(ex)
        }
    }

    private fun execute(statement: Stmt) = statement.accept(this)

    private fun stringify(value: Any?): String {
        return when (value) {
            null -> "nil"
            is Double -> {
                val text = value.toString()
                if (text.endsWith(".0")) {
                    text.substring(0, text.length - 2)
                } else {
                    text
                }
            }

            else -> value.toString()

        }
    }

    override fun visitAssignExpr(expr: Assign?): Any? {
        return expr?.let {
            val value = evaluate(expr.value)
            environment.assign(expr.name, value)
            return value
        }
    }

    override fun visitBinaryExpr(expression: Binary?): Any? {
        if (expression != null) {
            val left = evaluate(expression.left)
            val right = evaluate(expression.right)

            return if (areNumbers(left, right)) {
                val numLeft = left as Double
                val numRight = right as Double
                checkNumberOperand(expression.operator, left, right)
                return when (expression.operator.type) {
                    MINUS -> numLeft - numRight
                    SLASH -> numLeft / numRight
                    STAR -> numLeft * numRight
                    PLUS -> numLeft + numRight
                    GREATER -> numLeft > numRight
                    GREATER_EQUAL -> numLeft >= numRight
                    LESS -> numLeft < numRight
                    LESS_EQUAL -> numLeft <= numRight
                    else -> throw RuntimeError(expression.operator, "no operation configured")
                }

            } else if (areStrings(left, right)) {
                val strLeft = left as String
                val strRight = right as String
                return when (expression.operator.type) {
                    PLUS -> strLeft + strRight
                    else -> throw RuntimeError(expression.operator, "no operation configured")
                }
            } else {
                when (expression.operator.type) {
                    EQUAL_EQUAL -> isEqual(left, right)
                    BANG_EQUAL -> !isEqual(left, right)
                    else -> throw RuntimeError(expression.operator, "no operation configured")
                }
            }
        }
        return null
    }

    private fun isEqual(left: Any?, right: Any?): Boolean {
        return left == right
    }

    private fun areNumbers(vararg values: Any?): Boolean {
        return values.all { it != null } && values.all { it is Double }
    }

    private fun areStrings(vararg values: Any?): Boolean {
        return values.all { it != null } && values.all { it is String }
    }

    override fun visitGroupingExpr(expression: Grouping?) = expression?.let { evaluate(expression.expression) }

    private fun evaluate(expr: Expr) = expr.accept(this)

    override fun visitLiteralExpr(expression: Literal?) = expression?.value

    override fun visitUnaryExpr(expression: Unary?): Any? {
        if (expression != null) {
            val right = expression.right
            return when (expression.operator.type) {
                MINUS -> {
                    checkNumberOperand(expression.operator, right)
                    -(evaluate(right) as Double)
                }

                BANG -> !isTruthy(right)
                else -> throw RuntimeError(expression.operator, "no operation configured")
            }
        }
        return null
    }

    override fun visitVariableExpr(expr: Variable?): Any? {
        return expr?.let { environment.get(it.name) }
    }

    private fun isTruthy(any: Any?): Boolean {
        return when (any) {
            null -> false
            is Boolean -> any
            else -> true
        }
    }

    private fun checkNumberOperand(operator: Token, vararg operands: Any) {
        if (operands.any { it !is Double }) {
            throw RuntimeError(operator, "Operand must be a number.")
        }
    }

    override fun visitBlockStmt(stmt: Block?) {
        stmt?.let {
            executeBlock(stmt.statments, Environment(environment))
        }
    }

    private fun executeBlock(statments: List<Stmt>, environment: Environment) {
        val previous = this.environment
        try {
            this.environment = environment
            statments.forEach {
                execute(it)
            }
        } finally {
            this.environment = previous
        }
    }

    override fun visitExpressionStmt(stmt: Expression?) {
        if (stmt != null) {
            evaluate(stmt.expression)
        }
    }

    override fun visitPrintStmt(stmt: Print?) {
        if (stmt != null) {
            println(stringify(evaluate(stmt.expression)))
        }
    }

    override fun visitVarStmt(stmt: Var?) {
        if (stmt != null) {
            stmt.initializer?.let {
                environment.define(stmt.name.lexeme, evaluate(it))
            } ?: environment.define(stmt.name.lexeme, null)
        }
    }

}