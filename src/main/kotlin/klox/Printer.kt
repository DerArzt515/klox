package klox


class AstPrinter : Expr.Visitor<String> {
    fun print(expr: Expr) = expr.accept(this)
    override fun visitAssignExpr(expr: Assign?): String? {
        TODO("Not yet implemented")
    }

    override fun visitBinaryExpr(expression: Binary?): String? {
        return expression?.let {
            parenthesize(
                expression.operator.lexeme,
                expression.left,
                expression.right
            )
        }
    }


    override fun visitGroupingExpr(grouping: Grouping?): String? {
        return grouping?.let {
            parenthesize("group", it.expression)
        }
    }

    override fun visitLiteralExpr(literal: Literal?): String? {
        return literal?.value?.toString()
    }

    override fun visitUnaryExpr(unary: Unary?): String? {
        return unary?.let { parenthesize(unary.operator.lexeme, unary.right) }
    }

    override fun visitVariableExpr(expr: Variable?): String? {
        TODO("Not yet implemented")
    }

    private fun parenthesize(name: String, vararg exprs: Expr): String {
        return "($name" +
                exprs.map { it.accept(this) }
                    .fold(" ") { acc, next -> "$acc $next" } + ")"
    }
}

fun main() {
    val exp = Binary(
        Unary(
            Token(TokenType.MINUS, TokenType.MINUS.stringRep, null, 1),
            Literal(123)
        ),
        Token(TokenType.STAR, TokenType.STAR.stringRep, null, 1),
        Grouping(Literal(45.67))
    )
    println(AstPrinter().print(exp))
}