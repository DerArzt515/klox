import TokenType.*

val NEW_LINE = '\n'

class Scanner(
    private val source: String
) {
    private var start = 0
    private var current = 0
    private var line = 1
    private var tokens = mutableListOf<Token>()

    fun scanTokens(): List<Token> {

        while (notAtEnd()) {
            start = current
            scanToken()
        }
        tokens.add(Token(TokenType.EOF, "", null, line))
        return tokens.toList()
    }

    private fun scanToken() {
        when (val character = advance()) {
            SLASH.stringRep -> {
                if (isLineComment()) {
                    while (peek() != NEW_LINE && notAtEnd()) {
                        advance()
                    }
                } else {
                    addToken(SLASH)
                }
            }

            BANG.stringRep -> {
                addToken(if (match(EQUAL)) BANG_EQUAL else BANG)
            }

            EQUAL.stringRep -> {
                addToken(if (match(EQUAL)) EQUAL_EQUAL else EQUAL)
            }

            LESS.stringRep -> {
                addToken(if (match(EQUAL)) LESS_EQUAL else LESS)
            }

            GREATER.stringRep -> {
                addToken(if (match(EQUAL)) GREATER_EQUAL else GREATER)
            }

            STRING.stringRep -> string()
            LEFT_PAREN.stringRep -> addToken(LEFT_PAREN)
            RIGHT_PAREN.stringRep -> addToken(RIGHT_PAREN)
            LEFT_BRACE.stringRep -> addToken(LEFT_BRACE)
            RIGHT_BRACE.stringRep -> addToken(RIGHT_BRACE)
            COMMA.stringRep -> addToken(COMMA)
            DOT.stringRep -> addToken(DOT)
            MINUS.stringRep -> addToken(MINUS)
            PLUS.stringRep -> addToken(PLUS)
            SEMICOLON.stringRep -> addToken(SEMICOLON)
            STAR.stringRep -> addToken(STAR)
            " ", "\r", "\t" -> println("thing")
            "\n" -> line++
            else -> {
                if (isDigit(character)) {
                    number()
                } else if (isAlpha(character)) {
                    identifier()
                } else {
                    Lox.error(line, "Unexpected character [$character]")
                }
            }
        }
    }

    private fun identifier() {
        while (isAlphaNumeric(peek())) {
            advance()
        }
        val text = source.substring(start, current)
        addToken(keywords.getOrDefault(text, IDENTIFIER))
    }

    private fun isAlphaNumeric(peek: Char): Boolean {
        return isAlpha(peek.toString()) || isDigit(peek)
    }

    private fun isAlpha(character: String): Boolean {
        if (character.length != 1) {
            return false
        }
        val char = character[0]
        return char in 'a'..'z' ||
                char in 'A'..'Z' ||
                char == '_'
    }

    private fun number() {
        while (isDigit(peek())) {
            advance()
        }
        // look for a fractional part
        if (peek().toString() == DOT.stringRep && isDigit(peekNext())) {
            advance()
            while (isDigit(peek())) {
                advance()
            }
        }
        addToken(NUMBER, source.substring(start, current).toDouble())
    }

    private fun peekNext(): Char? {
        if (current + 1 >= source.length) {
            return null
        }
        return source[current + 1]
    }

    private fun isDigit(character: String?): Boolean {
        character?.let {
            if (character.length == 1) {
                return isDigit(character[0])
            }
        }
        return false
    }

    private fun isDigit(character: Char?): Boolean {
        character?.let {
            return it in '0'..'9'
        }
        return false
    }

    private fun string() {
        while (peek().toString() != STRING.stringRep && notAtEnd()) {
            if (peek() == NEW_LINE) {
                line++
            }
            advance()
        }
        if (isAtEnd()) {
            Lox.error(line, "Unterminated string")
            return
        }
        // closing the "
        advance()

        // trim the surounding quotes
        val value = source.substring(start + 1, current - 1)
        addToken(STRING, value)
    }

    private fun peek(): Char {
        if (isAtEnd()) {
            return 0.toChar()
        }
        return source[current]
    }

    private fun isLineComment(): Boolean {
        return match(SLASH)
    }

    private fun match(expected: TokenType): Boolean {
        if (isAtEnd()) {
            return false
        }
        if (source[current].toString() != expected.stringRep) {
            return false
        }
        current++
        return true
    }

    private fun advance() = source[current++].toString()

    private fun isAtEnd() = current >= source.length
    private fun notAtEnd() = !isAtEnd()

    private fun addToken(type: TokenType, literal: Any? = null) {
        tokens.add(Token(type, source.substring(start, current), literal, line))
    }

}
