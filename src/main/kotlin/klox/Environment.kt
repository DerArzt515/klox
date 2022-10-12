package klox

class Environment(
    val enclosing: Environment? = null,
    private var values: MutableMap<String, Any?> = mutableMapOf()
) {
    fun define(name: String, value: Any?) {
        values.put(name, value)
    }

    fun get(name: Token): Any? {
        return enclosing?.get(name) ?: run {
            if (name.lexeme !in values.keys) {
                throw RuntimeError(name, "Undefined variable '${name.lexeme}'.")
            }
            values[name.lexeme]
        }
    }

    fun assign(name: Token, value: Any?) {
        if (enclosing != null) {
            enclosing.assign(name, value)
        } else {
            if (name.lexeme !in values.keys) {
                throw RuntimeError(name, "Undefined variable '${name.lexeme}'.")
            }
            values[name.lexeme] = value
        }
    }
}