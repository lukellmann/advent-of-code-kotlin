package aoc2022

import AoCDay
import util.illegalInput

// https://adventofcode.com/2022/day/24
object Day24 : AoCDay<Int>(
    title = "Blizzard Basin",
    part1ExampleAnswer = 18,
    part1Answer = 297,
    part2ExampleAnswer = 54,
    part2Answer = 856,
) {
    private enum class Blizzard { UP, DOWN, LEFT, RIGHT }
    private typealias Blizzards = List<List<Set<Blizzard>>>

    private fun parseBlizzards(input: String): Triple<Blizzards, Int, Int> {
        val lines = input.lines()
        val startX = lines.first().indexOf('.') - 1
        val goalX = lines.last().indexOf('.') - 1
        val blizzards: Blizzards = List(size = lines.size - 2) { y ->
            val line = lines[y + 1]
            List(size = line.length - 2) { x ->
                when (val char = line[x + 1]) {
                    '.' -> emptySet()
                    '^' -> setOf(UP)
                    'v' -> setOf(DOWN)
                    '<' -> setOf(LEFT)
                    '>' -> setOf(RIGHT)
                    else -> illegalInput(char)
                }
            }
        }
        require(blizzards.none { row ->
            UP in row[startX] || DOWN in row[startX] || UP in row[goalX] || DOWN in row[goalX]
        }) { "Blizzard could run out of map" }
        return Triple(blizzards, startX, goalX)
    }

    private fun Blizzards.move(): Blizzards {
        val moved = List(size) { y -> List(this[y].size) { mutableSetOf<Blizzard>() } }
        for ((y, row) in this.withIndex()) {
            for ((x, blizzards) in row.withIndex()) {
                for (blizzard in blizzards) {
                    when (blizzard) {
                        UP -> moved[(y - 1).mod(size)][x]
                        DOWN -> moved[(y + 1) % size][x]
                        LEFT -> moved[y][(x - 1).mod(row.size)]
                        RIGHT -> moved[y][(x + 1) % row.size]
                    } += blizzard
                }
            }
        }
        return moved
    }

    private fun steps(
        blizzards: Blizzards,
        startX: Int,
        startY: Int,
        goalX: Int,
        goalY: Int,
    ): Pair<Int, Blizzards> {
        var curPos = List(blizzards.size) { y -> BooleanArray(blizzards[y].size) }
        var cur = blizzards
        for (minute in 1..<Int.MAX_VALUE) {
            cur = cur.move()
            // we would do the always possible move to out-of-bounds goal in this minute
            if (curPos[goalY][goalX]) return Pair(minute, cur)
            val nextPos = List(curPos.size) { y -> BooleanArray(curPos[y].size) }
            for ((y, row) in curPos.withIndex()) {
                for ((x, weCouldBeThere) in row.withIndex()) {
                    if (weCouldBeThere) {
                        if (cur[y][x].isEmpty()) nextPos[y][x] = true
                        if (y > 0 && cur[y - 1][x].isEmpty()) nextPos[y - 1][x] = true
                        if (y < curPos.lastIndex && cur[y + 1][x].isEmpty()) nextPos[y + 1][x] = true
                        if (x > 0 && cur[y][x - 1].isEmpty()) nextPos[y][x - 1] = true
                        if (x < row.lastIndex && cur[y][x + 1].isEmpty()) nextPos[y][x + 1] = true
                    }
                }
            }
            // we could wait forever at the out-of-bounds start, so we could be here after every minute
            if (cur[startY][startX].isEmpty()) nextPos[startY][startX] = true
            curPos = nextPos
        }
        error("Minute ran out of Int range")
    }

    override fun part1(input: String): Int {
        val (blizzards, startX, goalX) = parseBlizzards(input)
        return steps(blizzards, startX, startY = 0, goalX, goalY = blizzards.lastIndex).first
    }

    override fun part2(input: String): Int {
        val (b0, startX, goalX) = parseBlizzards(input)
        val startY = 0
        val goalY = b0.lastIndex
        val (a, b1) = steps(b0, startX, startY, goalX, goalY)
        val (b, b2) = steps(b1, goalX, goalY, startX, startY)
        val (c, _) = steps(b2, startX, startY, goalX, goalY)
        return a + b + c
    }
}
