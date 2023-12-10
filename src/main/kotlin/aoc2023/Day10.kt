package aoc2023

import AoCDay
import aoc2023.Day10.Facing.*
import util.Vec2
import util.illegalInput
import util.plus

// https://adventofcode.com/2023/day/10
object Day10 : AoCDay<Int>(
    title = "Pipe Maze",
    part1ExampleAnswer = 8,
    part1Answer = 6725,
    part2ExampleAnswer = null,
    part2Answer = null,
) {
    private enum class Facing(val dir: Vec2, val nextPipes: String) {
        NORTH(dir = Vec2(0, -1), nextPipes = "|7F"),
        EAST(dir = Vec2(1, 0), nextPipes = "-J7"),
        SOUTH(dir = Vec2(0, 1), nextPipes = "|LJ"),
        WEST(dir = Vec2(-1, 0), nextPipes = "-LF"),
    }

    private fun Char.nextFacing(facing: Facing) = when (this) {
        '|' -> when (facing) {
            NORTH, SOUTH -> facing
            EAST, WEST -> illegalInput(facing)
        }
        '-' -> when (facing) {
            EAST, WEST -> facing
            NORTH, SOUTH -> illegalInput(facing)
        }
        'L' -> when (facing) {
            SOUTH -> EAST
            WEST -> NORTH
            NORTH, EAST -> illegalInput(facing)
        }
        'J' -> when (facing) {
            EAST -> NORTH
            SOUTH -> WEST
            NORTH, WEST -> illegalInput(facing)
        }
        '7' -> when (facing) {
            NORTH -> WEST
            EAST -> SOUTH
            SOUTH, WEST -> illegalInput(facing)
        }
        'F' -> when (facing) {
            NORTH -> EAST
            WEST -> SOUTH
            EAST, SOUTH -> illegalInput(facing)
        }
        else -> illegalInput(this)
    }

    private fun findLoop(input: String): Map<Vec2, Set<Facing>> {
        val grid = input.lines()
        val s = run {
            val sRow = grid.indexOfFirst { 'S' in it }
            Vec2(x = grid[sRow].indexOf('S'), y = sRow)
        }
        outer@ for (f in Facing.entries) {
            val loop = HashMap<Vec2, Set<Facing>>()
            var facing = f
            var pos = s + f.dir
            while (true) {
                when (val pipe = grid.getOrNull(pos.y)?.getOrNull(pos.x)) {
                    null -> continue@outer // out of grid, no loop
                    'S' -> {
                        loop[pos] = setOf(f, facing)
                        return loop
                    }
                    in facing.nextPipes -> {
                        val before = facing
                        facing = pipe.nextFacing(facing)
                        loop[pos] = setOf(before, facing)
                        pos += facing.dir
                    }
                    else -> continue@outer // pipe doesn't connect with previous, no loop
                }
            }
        }
        error("Did not find loop")
    }

    override fun part1(input: String) = findLoop(input).size / 2

    override fun part2(input: String) = 0
}
