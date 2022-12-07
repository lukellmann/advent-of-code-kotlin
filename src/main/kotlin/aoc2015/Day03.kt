package aoc2015

import AoCDay
import util.illegalInput

// https://adventofcode.com/2015/day/3
object Day03 : AoCDay<Int>(
    title = "Perfectly Spherical Houses in a Vacuum",
    part1ExampleAnswer = 4,
    part1Answer = 2592,
    part2ExampleAnswer = 3,
    part2Answer = 2360,
) {
    private data class House(val x: Int, val y: Int)

    private val START = House(x = 0, y = 0)

    private fun nextHouse(current: House, direction: Char): House {
        val (x, y) = current
        return when (direction) {
            '^' -> House(x, y = y + 1)
            'v' -> House(x, y = y - 1)
            '>' -> House(x = x + 1, y)
            '<' -> House(x = x - 1, y)
            else -> illegalInput(direction)
        }
    }

    override fun part1(input: String): Int {
        var santa = START
        val visited = hashSetOf(START)

        for (direction in input) {
            santa = nextHouse(santa, direction)
            visited.add(santa)
        }

        return visited.size
    }

    override fun part2(input: String): Int {
        var santa = START
        var robot = START
        val visited = hashSetOf(START)

        var toggle = true
        for (direction in input) {
            val current = if (toggle) santa else robot
            val update = nextHouse(current, direction)
            visited.add(update)
            if (toggle) santa = update else robot = update
            toggle = !toggle
        }

        return visited.size
    }
}
