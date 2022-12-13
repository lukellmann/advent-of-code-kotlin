package aoc2015

import AoCDay
import util.illegalInput

// https://adventofcode.com/2015/day/18
object Day18 : AoCDay<Int>(
    title = "Like a GIF For Your Yard",
    part1Answer = 768,
    part2Answer = 781,
) {
    private fun parseConfiguration(input: String) = input
        .lines()
        .map { line ->
            line.map { char ->
                when (char) {
                    '#' -> true
                    '.' -> false
                    else -> illegalInput(char)
                }
            }.toBooleanArray()
        }
        .toTypedArray()

    // assumes square
    private fun Array<BooleanArray>.nextConfiguration(): Array<BooleanArray> {
        val size = size
        val next = Array(size) { BooleanArray(size) }
        for ((rowIdx, row) in withIndex()) {
            for (columnIdx in 0..<size) {
                var neighborsOn = 0
                for (r in (rowIdx - 1).coerceAtLeast(0)..<(rowIdx + 2).coerceAtMost(size)) {
                    for (c in (columnIdx - 1).coerceAtLeast(0)..<(columnIdx + 2).coerceAtMost(size)) {
                        if (r == rowIdx && c == columnIdx) continue
                        if (this[r][c]) neighborsOn++
                    }
                }
                next[rowIdx][columnIdx] = if (row[columnIdx]) neighborsOn == 2 || neighborsOn == 3 else neighborsOn == 3
            }
        }
        return next
    }

    override fun part1(input: String): Int {
        var config = parseConfiguration(input)
        repeat(100) {
            config = config.nextConfiguration()
        }
        return config.sumOf { row -> row.count { on -> on } }
    }

    override fun part2(input: String): Int {
        var config = parseConfiguration(input)
        val last = config.lastIndex
        config[0][0] = true
        config[0][last] = true
        config[last][0] = true
        config[last][last] = true
        repeat(100) {
            config = config.nextConfiguration()
            config[0][0] = true
            config[0][last] = true
            config[last][0] = true
            config[last][last] = true
        }
        return config.sumOf { row -> row.count { on -> on } }
    }
}
