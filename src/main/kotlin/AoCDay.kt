import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.useLines

val AOC_DAY_NAME_REGEX = Regex("""aoc(20(?:1[5-9]|[2-9]\d))\.Day([01]\d|2[0-5])""")
val INPUT_DIR = Path("input")

private fun readTextWithLFEndings(file: Path) = file.useLines { lines -> lines.joinToString(separator = "\n") }
private val String.isMultiline get() = lines().size > 1

abstract class AoCDay<out T : Any>(
    private val title: String,
    private val part1ExampleAnswer: T? = null,
    private val part1Answer: T?,
    private val part2ExampleAnswer: T? = null,
    private val part2Answer: T?,
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

    protected abstract fun part1(input: String): T
    protected abstract fun part2(input: String): T

    private fun checkAnswer(puzzle: String, expectedAnswer: T?, actualAnswer: T) {
        if (expectedAnswer != null) {
            check(expectedAnswer == actualAnswer) {
                "$year Day $day: $puzzle no longer produces the same answer as before, expected $expectedAnswer but " +
                    "got $actualAnswer"
            }
        }
    }

    fun printAnswers() {
        val inputDirForYear = INPUT_DIR / "aoc$year"

        val dayString = day.toString().padStart(2, '0')
        val exampleInput = if (part1ExampleAnswer != null || part2ExampleAnswer != null) {
            readTextWithLFEndings(inputDirForYear / "day$dayString-example.txt")
        } else null
        val input = readTextWithLFEndings(inputDirForYear / "day$dayString.txt")

        val actualPart1ExampleAnswer = if (part1ExampleAnswer != null) part1(exampleInput!!) else null
        val actualPart1Answer = part1(input)
        val actualPart2ExampleAnswer = if (part2ExampleAnswer != null) part2(exampleInput!!) else null
        val actualPart2Answer = part2(input)

        val outputPart1Example = actualPart1ExampleAnswer?.let {
            val out = it.toString()
            "part 1 example answer:${if (out.isMultiline) "\n$out" else "  $out"}\n"
        } ?: ""
        val outputPart1 = actualPart1Answer.let {
            val out = it.toString()
            "part 1 answer:${if (out.isMultiline) "\n$out" else "          $out"}\n"
        }
        val outputPart2Example = actualPart2ExampleAnswer?.let {
            val out = it.toString()
            "part 2 example answer:${if (out.isMultiline) "\n$out" else "  $out"}\n"
        } ?: ""
        val outputPart2 = actualPart2Answer.let {
            val out = it.toString()
            "part 2 answer:${if (out.isMultiline) "\n$out" else "          $out"}"
        }
        val output = "--- $year Day $day: $title ---\n$outputPart1Example$outputPart1$outputPart2Example$outputPart2"
        if (day == 1) repeat(3) { println() }
        println(output)
        println()

        if (actualPart1ExampleAnswer != null) {
            checkAnswer("part 1 example", expectedAnswer = part1ExampleAnswer, actualPart1ExampleAnswer)
        }
        checkAnswer("part 1", expectedAnswer = part1Answer, actualPart1Answer)
        if (actualPart2ExampleAnswer != null) {
            checkAnswer("part 2 example", expectedAnswer = part2ExampleAnswer, actualPart2ExampleAnswer)
        }
        checkAnswer("part 2", expectedAnswer = part2Answer, actualPart2Answer)
    }
}
