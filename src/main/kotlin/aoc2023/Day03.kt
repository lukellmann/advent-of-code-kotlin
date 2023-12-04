package aoc2023

import AoCDay

// https://adventofcode.com/2023/day/3
object Day03 : AoCDay<Int>(
    title = "Gear Ratios",
    part1ExampleAnswer = 4361,
    part1Answer = 553079,
    part2ExampleAnswer = 467835,
    part2Answer = 84363105,
) {
    private data class Point(val row: Int, val col: Int)
    private class Number(val value: Int, val row: Int, val startCol: Int, val endCol: Int)
    private data class EngineSchematic(val numbers: List<Number>, val symbols: Map<Point, Char>)

    private fun parseEngineSchematic(input: String): EngineSchematic {
        val numbers = mutableListOf<Number>()
        val symbols = hashMapOf<Point, Char>()
        for ((row, line) in input.lineSequence().withIndex()) {
            var number = 0
            var startCol = 0
            var endCol = -1
            for ((col, char) in line.withIndex()) {
                if (char in '0'..'9') {
                    number = (number * 10) + (char - '0')
                    endCol = col
                } else {
                    if (startCol <= endCol) {
                        numbers += Number(number, row, startCol, endCol)
                        number = 0
                    }
                    startCol = col + 1
                    if (char != '.') symbols[Point(row, col)] = char
                }
            }
            if (startCol <= endCol) numbers += Number(number, row, startCol, endCol)
        }
        return EngineSchematic(numbers, symbols)
    }

    private val Number.adjacentPoints
        get() = sequence {
            for (col in (startCol - 1)..(endCol + 1)) {
                yield(Point(row + 1, col))
                yield(Point(row - 1, col))
            }
            yield(Point(row, startCol - 1))
            yield(Point(row, endCol + 1))
        }

    private val EngineSchematic.partNumbers get() = numbers.filter { it.adjacentPoints.any(symbols::containsKey) }

    override fun part1(input: String) = parseEngineSchematic(input).partNumbers.sumOf { it.value }

    override fun part2(input: String): Int {
        val (numbers, symbols) = parseEngineSchematic(input)
        val asteriskAdjacent = symbols.filterValues { it == '*' }.keys.associateWith { mutableListOf<Number>() }
        for (number in numbers) {
            for (point in number.adjacentPoints) {
                asteriskAdjacent[point]?.add(number)
            }
        }
        return asteriskAdjacent.values.filter { it.size == 2 }.sumOf { (a, b) -> a.value * b.value }
    }
}
