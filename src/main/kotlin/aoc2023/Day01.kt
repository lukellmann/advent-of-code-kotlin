package aoc2023

import AoCDay
import util.illegalInput

// https://adventofcode.com/2023/day/1
object Day01 : AoCDay<Int>(
    title = "Trebuchet?!",
    part1ExampleAnswer = 142 + (11 + 11 + 22 + 33 + 42 + 24 + 77),
    part1Answer = 54597,
    part2ExampleAnswer = 142 + 281,
    part2Answer = 54504,
) {
    private val DIGIT_REGEX = Regex("[0-9]|one|two|three|four|five|six|seven|eight|nine")

    private fun digitToInt(digit: String) = when (digit) {
        "one" -> 1
        "two" -> 2
        "three" -> 3
        "four" -> 4
        "five" -> 5
        "six" -> 6
        "seven" -> 7
        "eight" -> 8
        "nine" -> 9
        else -> if (digit.length == 1 && digit[0] in '0'..'9') digit[0] - '0' else illegalInput(digit)
    }

    override fun part1(input: String) = input
        .lineSequence()
        .sumOf { line ->
            val firstDigit = line.first { it in '0'..'9' }
            val lastDigit = line.last { it in '0'..'9' }
            (firstDigit - '0') * 10 + (lastDigit - '0')
        }

    override fun part2(input: String) = input
        .lineSequence()
        .sumOf { line ->
            val firstDigit = DIGIT_REGEX.find(line)?.value ?: illegalInput(line)
            val lastDigit = line.indices
                .reversed()
                .firstNotNullOf { index -> DIGIT_REGEX.find(line, startIndex = index)?.value }
            digitToInt(firstDigit) * 10 + digitToInt(lastDigit)
        }
}
