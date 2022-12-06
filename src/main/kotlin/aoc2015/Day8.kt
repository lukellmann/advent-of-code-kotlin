package aoc2015

import AoCDay
import util.illegalInput

// https://adventofcode.com/2015/day/8
object Day8 : AoCDay<Int>(
    title = "Matchsticks",
    part1ExampleAnswer = (2 + 5 + 10 + 6) - (0 + 3 + 7 + 1),
    part1Answer = 1333,
    part2ExampleAnswer = (6 + 9 + 16 + 11) - (2 + 5 + 10 + 6),
    part2Answer = 2046,
) {
    override fun part1(input: String) = input
        .lineSequence()
        .sumOf { stringLiteral ->
            var charsInMemory = 0
            var index = 1
            while (index < stringLiteral.lastIndex) {
                charsInMemory++
                index += if (stringLiteral[index] == '\\') {
                    when (stringLiteral[index + 1]) {
                        '\\', '"' -> 2
                        'x' -> 4
                        else -> illegalInput(stringLiteral)
                    }
                } else 1
            }
            stringLiteral.length - charsInMemory
        }

    override fun part2(input: String) = input
        .lineSequence()
        .sumOf { stringLiteral ->
            val encodedChars = 2 + stringLiteral.sumOf { char ->
                val encodedChars = when (char) {
                    '"', '\\' -> 2
                    else -> 1
                }
                encodedChars
            }
            encodedChars - stringLiteral.length
        }
}
