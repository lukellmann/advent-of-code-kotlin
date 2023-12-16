package aoc2023

import AoCDay
import aoc2023.Day16.Dir.*
import util.Vec2
import util.illegalInput
import util.plus

// https://adventofcode.com/2023/day/16
object Day16 : AoCDay<Int>(
    title = "The Floor Will Be Lava",
    part1ExampleAnswer = 46,
    part1Answer = 7210,
    part2ExampleAnswer = 51,
    part2Answer = 7673,
) {
    private enum class Dir(val vec: Vec2) {
        UP(Vec2(0, -1)),
        DOWN(Vec2(0, 1)),
        RIGHT(Vec2(1, 0)),
        LEFT(Vec2(-1, 0)),
    }

    private fun energizedTiles(grid: List<String>, start: Vec2, startDir: Dir): Int {
        data class Beam(val pos: Vec2, val dir: Dir)

        val ys = grid.indices
        val xs = grid.first().indices
        val seen = HashSet<Beam>()
        var step = setOf(Beam(pos = start, dir = startDir))
        while (step.isNotEmpty()) {
            val next = HashSet<Beam>()
            for ((oldPos, oldDir) in step) {
                val pos = oldPos + oldDir.vec
                if (pos.x in xs && pos.y in ys) {
                    when (val char = grid[pos.y][pos.x]) {
                        '.' -> next += Beam(pos, oldDir)
                        '/' -> next += Beam(
                            pos,
                            dir = when (oldDir) {
                                UP -> RIGHT
                                DOWN -> LEFT
                                RIGHT -> UP
                                LEFT -> DOWN
                            },
                        )
                        '\\' -> next += Beam(
                            pos,
                            dir = when (oldDir) {
                                UP -> LEFT
                                DOWN -> RIGHT
                                RIGHT -> DOWN
                                LEFT -> UP
                            },
                        )
                        '|' -> when (oldDir) {
                            UP, DOWN -> next += Beam(pos, oldDir)
                            RIGHT, LEFT -> {
                                next += Beam(pos, UP)
                                next += Beam(pos, DOWN)
                            }
                        }
                        '-' -> when (oldDir) {
                            UP, DOWN -> {
                                next += Beam(pos, RIGHT)
                                next += Beam(pos, LEFT)
                            }
                            RIGHT, LEFT -> next += Beam(pos, oldDir)
                        }
                        else -> illegalInput(char)
                    }
                }
            }
            next.removeAll(seen)
            seen.addAll(next)
            step = next
        }
        return seen.distinctBy(Beam::pos).size
    }

    override fun part1(input: String) = energizedTiles(grid = input.lines(), start = Vec2(-1, 0), startDir = RIGHT)

    override fun part2(input: String): Int {
        val grid = input.lines()
        val yLimit = grid.size
        val xLimit = grid.first().length
        var max = -1
        for (y in 0..<yLimit) {
            max = maxOf(
                max,
                energizedTiles(grid, start = Vec2(-1, y), startDir = RIGHT),
                energizedTiles(grid, start = Vec2(xLimit, y), startDir = LEFT),
            )
        }
        for (x in 0..<xLimit) {
            max = maxOf(
                max,
                energizedTiles(grid, start = Vec2(x, -1), startDir = DOWN),
                energizedTiles(grid, start = Vec2(x, yLimit), startDir = UP),
            )
        }
        return max
    }
}
