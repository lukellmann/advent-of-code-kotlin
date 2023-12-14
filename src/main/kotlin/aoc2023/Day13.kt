package aoc2023

import AoCDay
import util.illegalInput

// https://adventofcode.com/2023/day/13
object Day13 : AoCDay<Int>(
    title = "Point of Incidence",
    part1ExampleAnswer = 405,
    part1Answer = 37718,
    part2ExampleAnswer = 400,
    part2Answer = 40995,
) {
    private class Pattern(private val pattern: List<String>) {
        init {
            require(pattern.isNotEmpty()) { "Pattern must not be empty" }
            require(pattern.all(String::isNotEmpty)) { "Rows must not be empty" }
            require(pattern.map(String::length).distinct().size == 1) { "All rows must have the same length" }
        }

        fun replace(row: Int, col: Int) = Pattern(pattern.mapIndexed { iRow, r ->
            r.mapIndexed { iCol, c ->
                if (iRow == row && iCol == col) when (c) {
                    '.' -> '#'
                    '#' -> '.'
                    else -> illegalInput(c)
                } else c
            }.joinToString("")
        })

        val rowCount get() = pattern.size
        val colCount get() = pattern.first().length
        val rows get() = pattern
        val cols get() = pattern.first().indices.map { i -> pattern.map { row -> row[i] }.joinToString("") }
    }

    private fun parsePatterns(input: String) = input.split("\n\n").map(String::lines).map(::Pattern)

    private fun getReflections(list: List<String>) = (1..list.lastIndex)
        .filter { x -> (list.take(x).reversed() zip list.drop(x)).all { (a, b) -> a == b } }

    override fun part1(input: String) = parsePatterns(input).sumOf { pattern ->
        getReflections(pattern.rows).singleOrNull()?.let(100::times) ?: getReflections(pattern.cols).single()
    }

    override fun part2(input: String): Int {
        val patterns = parsePatterns(input)
        var sum = 0
        outer@ for (pattern in patterns) {
            val rowReflection = getReflections(pattern.rows).singleOrNull()
            val colReflection = getReflections(pattern.cols).singleOrNull()
            for (row in 0..<pattern.rowCount) {
                for (col in 0..<pattern.colCount) {
                    val replaced = pattern.replace(row, col)
                    val rr = getReflections(replaced.rows).filter { it != rowReflection }
                    if (rr.isNotEmpty()) {
                        sum += rr.single() * 100
                        continue@outer
                    }
                    val cr = getReflections(replaced.cols).filter { it != colReflection }
                    if (cr.isNotEmpty()) {
                        sum += cr.single()
                        continue@outer
                    }
                }
            }
        }
        return sum
    }
}
