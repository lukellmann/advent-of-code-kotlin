package aoc2022

import AoCDay
import util.illegalInput
import kotlin.math.abs
import kotlin.math.sign

// https://adventofcode.com/2022/day/9
object Day09 : AoCDay<Int>(
    title = "Rope Bridge",
    part1ExampleAnswer = 13,
    part1Answer = 6067,
    part2ExampleAnswer = 1,
    part2Answer = 2471,
) {
    private data class Position(val x: Int, val y: Int)

    private val STARTING_POSITION = Position(x = 0, y = 0)

    private fun simulateRopeAndCountTailPositions(headMotions: String, knots: Int): Int {
        val knotPositions = MutableList(size = knots) { STARTING_POSITION }
        val tailPositions = hashSetOf(knotPositions.last())
        headMotions
            .lineSequence()
            .map { motion -> motion.split(' ', limit = 2) }
            .forEach { (direction, steps) ->
                repeat(steps.toInt()) {
                    val (xh, yh) = knotPositions[0] // head
                    knotPositions[0] = when (direction) {
                        "U" -> Position(x = xh, y = yh + 1)
                        "D" -> Position(x = xh, y = yh - 1)
                        "R" -> Position(x = xh + 1, y = yh)
                        "L" -> Position(x = xh - 1, y = yh)
                        else -> illegalInput(direction)
                    }
                    for (i in 1..knotPositions.lastIndex) {
                        val prev = knotPositions[i - 1]
                        val (x, y) = knotPositions[i]
                        val dx = prev.x - x
                        val dy = prev.y - y
                        if (abs(dx) > 1 || abs(dy) > 1) {
                            knotPositions[i] = Position(x = x + dx.sign, y = y + dy.sign)
                        } else {
                            return@repeat // rest of the tail won't move either -> continue with next head motion
                        }
                    }
                    tailPositions.add(knotPositions.last())
                }
            }
        return tailPositions.size
    }

    override fun part1(input: String) = simulateRopeAndCountTailPositions(headMotions = input, knots = 2)

    override fun part2(input: String) = simulateRopeAndCountTailPositions(headMotions = input, knots = 10)
}
