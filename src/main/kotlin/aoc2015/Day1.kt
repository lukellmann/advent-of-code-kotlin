package aoc2015

import AoCDay
import util.illegalChar

// https://adventofcode.com/2015/day/1
object Day1 : AoCDay<Int>(
    title = "Not Quite Lisp",
    part1ExampleAnswer = 3,
    part1Answer = 280,
    part2ExampleAnswer = 1,
    part2Answer = 1797,
) {
    private val Char.direction
        get() = when (this) {
            '(' -> +1
            ')' -> -1
            else -> illegalChar(this)
        }

    override fun part1(input: String) = input.fold(initial = 0) { floor, char -> floor + char.direction }

    override fun part2(input: String): Int {
        var floor = 0
        for ((index, char) in input.withIndex()) {
            floor += char.direction
            if (floor == -1) return index + 1 // position is 1-based
        }
        error("Santa never entered the basement, he ended up on floor $floor")
    }
}
