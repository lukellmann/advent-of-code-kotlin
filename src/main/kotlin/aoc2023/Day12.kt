package aoc2023

import AoCDay

// https://adventofcode.com/2023/day/12
object Day12 : AoCDay<Int>(
    title = "Hot Springs",
    part1ExampleAnswer = 21,
    part1Answer = 7286,
    part2ExampleAnswer = null,
    part2Answer = null,
) {
    private fun parseRow(row: String): Pair<String, List<Int>> {
        val (springs, d) = row.split(' ', limit = 2)
        val damaged = d.split(',').map(String::toInt)
        return Pair(springs, damaged)
    }

    private fun countArrangements(springs: String, damaged: List<Int>): Int {
        val len = springs.length
        val damagedSum = damaged.sum()
        return (0..<(1 shl len)).count { combination ->
            if (combination.countOneBits() != damagedSum) return@count false
            val combinationAsString = buildString(len) {
                for (i in 0..<len) append(if (combination and (1 shl i) == 0) '.' else '#')
            }
            var ok = true
            for ((i, c) in springs.withIndex()) {
                when (c) {
                    '.', '#' -> if (combinationAsString[i] != c) {
                        ok = false
                        break
                    }
                }
            }
            ok && (combinationAsString.split('.').filterNot(String::isBlank).map(String::length) == damaged)
        }
    }

    override fun part1(input: String) = input
        .lineSequence()
        .map(::parseRow)
        .sumOf { (springs, damaged) -> countArrangements(springs, damaged) }

    // TODO, brute-forcing won't work
    override fun part2(input: String) = 0
}
