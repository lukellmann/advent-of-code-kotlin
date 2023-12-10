package aoc2023

import AoCDay
import aoc2023.Day10.Dir.*
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
    private enum class Dir(val facing: Vec2, val next: String) {
        NORTH(facing = Vec2(0, -1), next = "|7F"),
        EAST(facing = Vec2(1, 0), next = "-J7"),
        SOUTH(facing = Vec2(0, 1), next = "|LJ"),
        WEST(facing = Vec2(-1, 0), next = "-LF"),
    }

    private operator fun List<String>.get(pos: Vec2) = getOrNull(pos.y)?.getOrNull(pos.x)

    private fun Char.nextDir(facing: Dir): Dir = when (this) {
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

    private fun loopSize(grid: List<String>, sRow: Int, sCol: Int): Int {
        outer@ for (dir in Dir.entries) {
            var pos = Vec2(sCol, sRow)
            var facing = dir
            var size = 1
            while (true) {
                pos += facing.facing
                when (val next = grid[pos]) {
                    'S' -> return size
                    null -> continue@outer
                    in facing.next -> {
                        facing = next.nextDir(facing)
                        size++
                    }
                    else -> continue@outer
                }
            }
        }
        error("Did not find loop")
    }

    override fun part1(input: String): Int {
        val grid = input.lines()
        val sRow = grid.indexOfFirst { 'S' in it }
        val sCol = grid[sRow].indexOf('S')
        return loopSize(grid, sRow, sCol) / 2
    }

    override fun part2(input: String) = 0
}
