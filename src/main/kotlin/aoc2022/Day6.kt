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
    private class Window(val index: Int, val uniqueChars: Int)

    private fun countCharsUntilNUniqueChars(signal: String, n: Int) = signal
        .windowedSequence(size = n, step = 1)
        .mapIndexed { index, window -> Window(index, uniqueChars = window.toSet().size) }
        .first { it.uniqueChars == n }
        .index + n

    override fun part1(input: String) = countCharsUntilNUniqueChars(signal = input, n = 4)

    override fun part2(input: String) = countCharsUntilNUniqueChars(signal = input, n = 14)
}
