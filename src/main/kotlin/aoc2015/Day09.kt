package aoc2015

import AoCDay
import util.match

// https://adventofcode.com/2015/day/9
object Day09 : AoCDay<Int>(
    title = "All in a Single Night",
    part1ExampleAnswer = 605,
    part1Answer = 207,
    part2ExampleAnswer = 982,
    part2Answer = 804,
) {
    private fun <T> List<T>.permutations(): Sequence<List<T>> {
        val me = this
        if (me.size <= 1) return sequenceOf(me)
        return sequence {
            for (perm in me.subList(1, me.size).permutations()) {
                for (i in me.indices) {
                    yield(perm.subList(0, i) + me[0] + perm.subList(i, perm.size))
                }
            }
        }
    }

    private val DISTANCE_REGEX = Regex("""(\w+) to (\w+) = (\d+)""")

    private fun distancesForAllRoutes(input: String): Sequence<Int> {
        val distances = input
            .lineSequence()
            .map { line -> DISTANCE_REGEX.match(line) }
            .associate { (a, b, distance) -> setOf(a, b) to distance.toInt() }
        val locations = distances.keys.flatten().distinct()
        return locations
            .permutations()
            .map { route -> route.windowed(size = 2, step = 1) { (a, b) -> distances.getValue(setOf(a, b)) }.sum() }
    }

    override fun part1(input: String) = distancesForAllRoutes(input).min()

    override fun part2(input: String) = distancesForAllRoutes(input).max()
}
