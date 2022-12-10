package aoc2015

import AoCDay
import util.match
import util.permutations

// https://adventofcode.com/2015/day/9
object Day09 : AoCDay<Int>(
    title = "All in a Single Night",
    part1ExampleAnswer = 605,
    part1Answer = 207,
    part2ExampleAnswer = 982,
    part2Answer = 804,
) {
    private val DISTANCE_REGEX = Regex("""(\w+) to (\w+) = (\d+)""")

    private fun distancesForAllRoutes(input: String): Sequence<Int> {
        val distances = input
            .lineSequence()
            .map { line -> DISTANCE_REGEX.match(line) }
            .associate { (a, b, distance) -> setOf(a, b) to distance.toInt() }
        val locations = distances.keys.flatten().distinct()
        return locations
            .permutations()
            .map { route -> route.windowed(size = 2, step = 1) { (a, b) -> distances[setOf(a, b)]!! }.sum() }
    }

    override fun part1(input: String) = distancesForAllRoutes(input).min()

    override fun part2(input: String) = distancesForAllRoutes(input).max()
}
