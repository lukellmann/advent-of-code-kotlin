package aoc2023

import AoCDay
import kotlin.math.abs

// https://adventofcode.com/2023/day/11
object Day11 : AoCDay<Long>(
    title = "Cosmic Expansion",
    part1ExampleAnswer = 374,
    part1Answer = 9639160,
    part2Answer = 752936133304,
) {
    private data class Galaxy(val x: Long, val y: Long)

    private fun parseUniverse(image: String) = image
        .lineSequence()
        .withIndex()
        .flatMap { (y, line) ->
            line.withIndex().filter { (_, char) -> char == '#' }.map { (x, _) -> Galaxy(x.toLong(), y.toLong()) }
        }
        .toSet()

    private fun manhattanDistance(g1: Galaxy, g2: Galaxy) = abs(g1.x - g2.x) + abs(g1.y - g2.y)

    private fun getSumOfShortestPathLengths(image: String, expansion: Int): Long {
        val universe = parseUniverse(image)
        val emptyXs = (0..<universe.maxOf(Galaxy::x)).filter { x -> universe.none { it.x == x } }
        val emptyYs = (0..<universe.maxOf(Galaxy::y)).filter { y -> universe.none { it.y == y } }
        val expanded = universe.map { (x, y) ->
            val xExpansion = (expansion - 1) * emptyXs.count { it < x }
            val yExpansion = (expansion - 1) * emptyYs.count { it < y }
            Galaxy(x + xExpansion, y + yExpansion)
        }
        return expanded.withIndex().sumOf { (i, g1) -> expanded.take(i).sumOf { g2 -> manhattanDistance(g1, g2) } }
    }

    override fun part1(input: String) = getSumOfShortestPathLengths(image = input, expansion = 2)

    override fun part2(input: String) = getSumOfShortestPathLengths(image = input, expansion = 1_000_000)
}
