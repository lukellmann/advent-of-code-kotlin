package aoc2022

import AoCDay

// https://adventofcode.com/2022/day/14
object Day14 : AoCDay<Int>(
    title = "Regolith Reservoir",
    part1ExampleAnswer = 24,
    part1Answer = 817,
    part2ExampleAnswer = 93,
    part2Answer = 23416,
) {
    private data class Point(val x: Int, val y: Int)

    private val SAND_SOURCE = Point(x = 500, y = 0)

    private fun parseRockPaths(input: String) = input
        .lineSequence()
        .map { rockPath ->
            rockPath.split(" -> ").map { point ->
                val (x, y) = point.split(',', limit = 2)
                Point(x.toInt(), y.toInt())
            }
        }

    private fun MutableSet<Point>.fillWithRocks(rockPaths: Sequence<List<Point>>) {
        for (rockPath in rockPaths) {
            for ((from, to) in rockPath.zipWithNext()) {
                val xs = if (from.x < to.x) from.x..to.x else to.x..from.x
                val ys = if (from.y < to.y) from.y..to.y else to.y..from.y
                for (x in xs) {
                    for (y in ys) {
                        add(Point(x, y))
                    }
                }
            }
        }
    }

    override fun part1(input: String): Int {
        val points = HashSet<Point>()
        points.fillWithRocks(parseRockPaths(input))

        val rockCount = points.size
        val yLowestRock = points.maxOf { it.y }

        var sandUnit = SAND_SOURCE
        while (sandUnit.y < yLowestRock) { // once sandUnit.y == yLowestRock it will fall forever
            val x = sandUnit.x
            val yDown = sandUnit.y + 1
            var p: Point
            sandUnit = when {
                Point(x, yDown).also { p = it } !in points -> p
                Point(x - 1, yDown).also { p = it } !in points -> p
                Point(x + 1, yDown).also { p = it } !in points -> p
                else -> {
                    points += sandUnit // sand unit comes to rest
                    SAND_SOURCE // create next sand unit
                }
            }
        }

        return points.size - rockCount
    }

    override fun part2(input: String): Int {
        val points = HashSet<Point>()
        points.fillWithRocks(parseRockPaths(input))

        val rockCount = points.size
        val yFloor = points.maxOf { it.y } + 2

        var sandUnit = SAND_SOURCE
        while (true) {
            val x = sandUnit.x
            val yDown = sandUnit.y + 1
            var p: Point
            sandUnit = when {
                yDown == yFloor -> {
                    points += sandUnit // sand unit comes to rest on the floor
                    SAND_SOURCE // create next sand unit
                }
                Point(x, yDown).also { p = it } !in points -> p
                Point(x - 1, yDown).also { p = it } !in points -> p
                Point(x + 1, yDown).also { p = it } !in points -> p
                else -> {
                    points += sandUnit // sand unit comes to rest
                    if (sandUnit == SAND_SOURCE) break // source is blocked
                    SAND_SOURCE // create next sand unit
                }
            }
        }

        return points.size - rockCount
    }
}
