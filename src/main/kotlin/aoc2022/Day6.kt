package aoc2022

import AoCDay

// https://adventofcode.com/2022/day/6
object Day6 : AoCDay<Int>(
    title = "Tuning Trouble",
    part1ExampleAnswer = 7,
    part1Answer = 1356,
    part2ExampleAnswer = 19,
    part2Answer = 2564,
) {
    private fun countCharsUntilNUniqueChars(signal: String, n: Int) = signal
        .windowedSequence(size = n, step = 1)
        .indexOfFirst { window -> window.toSet().size == n } + n

    override fun part1(input: String) = countCharsUntilNUniqueChars(signal = input, n = 4)

    override fun part2(input: String) = countCharsUntilNUniqueChars(signal = input, n = 14)
}
