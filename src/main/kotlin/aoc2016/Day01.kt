package aoc2016

import AoCDay
import util.Vec2
import util.illegalInput
import util.plus
import util.times
import kotlin.math.abs

// https://adventofcode.com/2016/day/1
object Day01 : AoCDay<Int>(
    title = "No Time for a Taxicab",
    part1ExampleAnswer = 8,
    part1Answer = 252,
    part2ExampleAnswer = 4,
    part2Answer = 143,
) {
    private fun Vec2.manhattanDistanceFromStart() = abs(x) + abs(y)

    private enum class Dir(val vec: Vec2) {
        NORTH(Vec2(1, 0)),
        EAST(Vec2(0, 1)),
        SOUTH(Vec2(-1, 0)),
        WEST(Vec2(0, -1)),
    }

    private fun Dir.turn(dir: Char): Dir = when (dir) {
        'L' -> when (this) {
            NORTH -> WEST
            EAST -> NORTH
            SOUTH -> EAST
            WEST -> SOUTH
        }
        'R' -> when (this) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
        }
        else -> illegalInput(dir)
    }

    override fun part1(input: String) = input
        .splitToSequence(", ")
        .fold(
            initial = Pair(Vec2(0, 0), Dir.NORTH),
        ) { (pos, facing), ins ->
            val newFacing = facing.turn(ins.first())
            val step = ins.drop(1).toInt()
            val newPos = pos + (newFacing.vec * step)
            Pair(newPos, newFacing)
        }
        .first
        .manhattanDistanceFromStart()

    override fun part2(input: String): Int {
        var pos = Vec2(0, 0)
        var facing = Dir.NORTH
        val visited = hashSetOf(pos)
        outer@ for (ins in input.splitToSequence(", ")) {
            facing = facing.turn(ins.first())
            repeat(ins.drop(1).toInt()) {
                pos += facing.vec
                if (!visited.add(pos)) break@outer
            }
        }
        return pos.manhattanDistanceFromStart()
    }
}
