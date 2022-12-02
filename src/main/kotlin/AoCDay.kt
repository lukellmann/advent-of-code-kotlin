import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.useLines

val AOC_DAY_NAME_REGEX = Regex("""aoc(20(?:1[5-9]|[2-9]\d)).Day(\d|1\d|2[0-5])""")
val INPUT_DIR = Path("input")

private fun readTextWithLFEndings(file: Path) = file.useLines { lines -> lines.joinToString(separator = "\n") }

abstract class AoCDay(
    private val title: String,
    private val part1ExampleAnswer: Int,
    private val part1Answer: Int? = null,
    private val part2ExampleAnswer: Int? = null,
    private val part2Answer: Int? = null,
) {
    private val year: Int
    private val day: Int

    init {
        val name = this::class.qualifiedName ?: error("AoCDay should not be extended by local or anonymous class")
        val matchResult = AOC_DAY_NAME_REGEX.matchEntire(name) ?: error("Illegal name for AoCDay subtype: $name")
        val (y, d) = matchResult.destructured
        year = y.toInt()
        day = d.toInt()
    }

    @Volatile
    private var part1IsImplemented: Boolean = true
    protected open fun part1(input: String): Int {
        part1IsImplemented = false
        return 0
    }

    @Volatile
    private var part2IsImplemented: Boolean = true
    protected open fun part2(input: String): Int {
        part2IsImplemented = false
        return 0
    }

    private fun checkAnswer(puzzle: String, expectedAnswer: Int?, actualAnswer: Int) {
        if (expectedAnswer != null) {
            check(expectedAnswer == actualAnswer) {
                "$year Day $day: $puzzle no longer produces the same answer as before, expected $expectedAnswer but " +
                        "got $actualAnswer"
            }
        }
    }

    fun printAnswers() {
        val inputDirForYear = INPUT_DIR / "aoc$year"

        val exampleInput = readTextWithLFEndings(inputDirForYear / "day$day-example.txt")
        val input = readTextWithLFEndings(inputDirForYear / "day$day.txt")

        val actualPart1ExampleAnswer = part1(exampleInput)
        val actualPart1Answer = part1(input)
        val actualPart2ExampleAnswer = part2(exampleInput)
        val actualPart2Answer = part2(input)

        if (part1IsImplemented) {
            checkAnswer("part 1 example", expectedAnswer = part1ExampleAnswer, actualPart1ExampleAnswer)
            checkAnswer("part 1", expectedAnswer = part1Answer, actualPart1Answer)
        }
        if (part2IsImplemented) {
            checkAnswer("part 2 example", expectedAnswer = part2ExampleAnswer, actualPart2ExampleAnswer)
            checkAnswer("part 2", expectedAnswer = part2Answer, actualPart2Answer)
        }

        val output = """
            --- $year Day $day: $title ---
            ${if (part1IsImplemented) "part 1 example answer:  $actualPart1ExampleAnswer" else "part 1 not yet implemented"}
            ${if (part1IsImplemented) "part 1 answer:          $actualPart1Answer" else "part 1 not yet implemented"}
            ${if (part2IsImplemented) "part 2 example answer:  $actualPart2ExampleAnswer" else "part 2 not yet implemented"}
            ${if (part2IsImplemented) "part 2 answer:          $actualPart2Answer" else "part 2 not yet implemented"}
        """.trimIndent()

        println(output)
        println()
    }
}
