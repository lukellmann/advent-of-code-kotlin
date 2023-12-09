package aoc2023

import AoCDay

// https://adventofcode.com/2023/day/9
object Day09 : AoCDay<Int>(
    title = "Mirage Maintenance",
    part1ExampleAnswer = 114,
    part1Answer = 2075724761,
    part2ExampleAnswer = 2,
    part2Answer = 1072,
) {
    private fun parseHistory(input: String) = input.split(' ').map(String::toInt)

    private fun extrapolateNextValue(history: List<Int>): Int {
        val lasts = mutableListOf(history.last())
        var cur = history
        while (cur.any { it != 0 }) {
            cur = cur.zipWithNext { a, b -> b - a }
            if (cur.isNotEmpty()) lasts.add(cur.last())
        }
        return lasts.sum()
    }

    override fun part1(input: String) = input
        .lineSequence()
        .map(::parseHistory)
        .sumOf(::extrapolateNextValue)

    override fun part2(input: String) = input
        .lineSequence()
        .map { line -> parseHistory(line).reversed() }
        .sumOf(::extrapolateNextValue)
}
