import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ScannerTest : DescribeSpec({
    describe("scanTokens") {
        it("should parse all known tokens") {
            val scanner = Scanner(
                TokenType
                    .values()
                    .map { it.stringRep }
                    .fold("") { acc, next -> "$acc $next" }
            )

            val tokens = scanner.scanTokens()

            tokens.size shouldBe TokenType.values().size

        }
    }
})
