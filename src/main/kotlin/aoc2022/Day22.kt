package aoc2022

import AoCDay
import aoc2022.Day22.Space.*
import util.illegalInput

// https://adventofcode.com/2022/day/22
object Day22 : AoCDay<Int>(
    title = "Monkey Map",
    part1ExampleAnswer = 6032,
    part1Answer = 50412,
    part2ExampleAnswer = null,
    part2Answer = null,
) {
    private enum class Space { EMPTY, TILE, WALL }

    private fun parseMap(input: String): Array<Array<Space>> {
        val lines = input.lines()
        val maxLength = lines.maxOf { it.length }
        val map = Array(lines.size) { Array(size = maxLength) { EMPTY } }
        for ((row, line) in lines.withIndex()) {
            for ((col, char) in line.withIndex()) {
                map[row][col] = when (char) {
                    ' ' -> EMPTY
                    '.' -> TILE
                    '#' -> WALL
                    else -> illegalInput(input)
                }
            }
        }
        return map
    }

    private sealed interface Path {
        object Right : Path
        object Left : Path
        class Number(val number: Int) : Path
    }

    private fun parsePath(input: String) = sequence {
        val num = StringBuilder()
        suspend fun SequenceScope<Path>.yieldNum() {
            if (num.isNotEmpty()) {
                yield(Path.Number(num.toString().toInt()))
                num.clear()
            }
        }
        for (char in input) {
            when (char) {
                'R' -> {
                    yieldNum()
                    yield(Path.Right)
                }
                'L' -> {
                    yieldNum()
                    yield(Path.Left)
                }
                in '0'..'9' -> num.append(char)
                else -> illegalInput(input)
            }
        }
        yieldNum()
    }

    private enum class Direction { RIGHT, DOWN, LEFT, UP }

    override fun part1(input: String): Int {
        val (m, p) = input.split("\n\n", limit = 2)
        val map = parseMap(m)
        var row = 0
        var col = map[row].indexOf(TILE)
        var facing = Direction.RIGHT
        for (path in parsePath(p)) {
            when (path) {
                Path.Right -> facing = when (facing) {
                    RIGHT -> DOWN
                    DOWN -> LEFT
                    LEFT -> UP
                    UP -> RIGHT
                }
                Path.Left -> facing = when (facing) {
                    RIGHT -> UP
                    DOWN -> RIGHT
                    LEFT -> DOWN
                    UP -> LEFT
                }
                is Path.Number -> repeat(path.number) {
                    when (facing) {
                        RIGHT -> {
                            val r = map[row]
                            var c = (col + 1) % r.size
                            while (r[c] == EMPTY) c = (c + 1) % r.size
                            if (r[c] == TILE) col = c
                        }
                        DOWN -> {
                            var r = (row + 1) % map.size
                            while (map[r][col] == EMPTY) r = (r + 1) % map.size
                            if (map[r][col] == TILE) row = r
                        }
                        LEFT -> {
                            val r = map[row]
                            var c = (col - 1).mod(r.size)
                            while (r[c] == EMPTY) c = (c - 1).mod(r.size)
                            if (r[c] == TILE) col = c
                        }
                        UP -> {
                            var r = (row - 1).mod(map.size)
                            while (map[r][col] == EMPTY) r = (r - 1).mod(map.size)
                            if (map[r][col] == TILE) row = r
                        }
                    }
                }
            }
        }
        return 1000 * (row + 1) + 4 * (col + 1) + facing.ordinal
    }

    // for future me
    override fun part2(input: String) = 0
}
