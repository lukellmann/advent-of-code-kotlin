package aoc2016

import AoCDay

// https://adventofcode.com/2016/day/3
object Day03 : AoCDay<Int>(
    title = "Squares With Three Sides",
    part1Answer = 917,
    part2Answer = 1649,
) {
    private val SPACE = Regex("""\s+""")
    private fun parseLine(line: String) = line.trim().split(SPACE, limit = 3).map(String::toInt)

    private fun isTrianglePossible(sides: List<Int>): Boolean {
        val (a, b, c) = sides
        return a + b > c && a + c > b && b + c > a
    }

    override fun part1(input: String) = input
        .lineSequence()
        .map(::parseLine)
        .count(::isTrianglePossible)

    override fun part2(input: String) = input
        .lineSequence()
        .map(::parseLine)
        .chunked(3)
        .flatMap { chunk -> List(3) { i -> listOf(chunk[0][i], chunk[1][i], chunk[2][i]) } }
        .count(::isTrianglePossible)
}
