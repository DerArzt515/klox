package klox

abstract class Expression
data class Binary(
    val right: Expression,
    val operator: Token,
    val left: Expression,
) : Expression()

data class Grouping(
    val expression: Expression,
) : Expression()

data class Literal(
    val value: Any,
) : Expression()

data class Unary(
    val right: Expression,
    val operator: Token,
) : Expression()
