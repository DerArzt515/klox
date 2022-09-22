import TokenType.*

enum class TokenType(val stringRep: String) {
    // Single-character tokens.
    LEFT_PAREN("("),
    RIGHT_PAREN(")"),
    LEFT_BRACE("{"),
    RIGHT_BRACE("}"),
    COMMA(","),
    DOT("."),
    MINUS("-"),
    PLUS("+"),
    SEMICOLON(";"),
    SLASH("/"),
    STAR("*"),

    // One or two character tokens.
    BANG("!"),
    BANG_EQUAL("!="),
    EQUAL("="),
    EQUAL_EQUAL("=="),
    GREATER(">"),
    GREATER_EQUAL(">="),
    LESS("<"),
    LESS_EQUAL("<="),

    // Literals.
    IDENTIFIER(""),
    STRING("\""),
    NUMBER(""),

    // Keywords.
    AND("and"),
    CLASS("class"),
    ELSE("else"),
    FALSE("false"),
    FUN("fun"),
    FOR("for"),
    IF("if"),
    NIL("nil"),
    OR("or"),

    PRINT("print"),
    RETURN("return"),
    SUPER("super"),
    THIS("this"),
    TRUE("true"),
    VAR("var"),
    WHILE("while"),

    EOF("EOF")
}

val keywords = mapOf(
    AND.stringRep to AND,
    CLASS.stringRep to CLASS,
    ELSE.stringRep to ELSE,
    FALSE.stringRep to FALSE,
    FUN.stringRep to FUN,
    FOR.stringRep to FOR,
    IF.stringRep to IF,
    NIL.stringRep to NIL,
    OR.stringRep to OR,
    PRINT.stringRep to PRINT,
    RETURN.stringRep to RETURN,
    SUPER.stringRep to SUPER,
    THIS.stringRep to THIS,
    TRUE.stringRep to TRUE,
    VAR.stringRep to VAR,
    WHILE.stringRep to WHILE
)
