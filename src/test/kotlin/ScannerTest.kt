import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import klox.Scanner
import klox.TokenType

class ScannerTest : DescribeSpec({
    describe("scanTokens") {
        it("should parse all known tokens") {
            val scanner = Scanner(
                TokenType
                    .values()
                    .map { it.stringRep }
                    .filter { it != TokenType.STRING.stringRep }
                    .addTo("\"words\"")
                    .addTo(1)
                    .addTo(2.3)
                    .fold("") { acc, next -> "$acc $next" }
            )

            val tokens = scanner.scanTokens()

            val types = tokens.map { it.type }

            TokenType.values()
                .filter { it != TokenType.STRING }
                .map { types shouldContain it }
        }
    }
})

fun Collection<Any>.addTo(element: Any): Collection<Any> {
    return this + element
}