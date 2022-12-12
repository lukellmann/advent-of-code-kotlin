package aoc2022

import AoCDay

// https://adventofcode.com/2022/day/12
object Day12 : AoCDay<Int>(
    title = "Hill Climbing Algorithm",
    part1ExampleAnswer = 31,
    part1Answer = 462,
    part2ExampleAnswer = 29,
    part2Answer = 451,
) {
    private class Position(val x: Int, val y: Int)

    private class Heightmap private constructor(private val grid: Array<CharArray>) {
        val xSize = grid[0].size
        val ySize = grid.size
        operator fun get(x: Int, y: Int) = grid[y][x]

        companion object {
            fun parse(input: String): Triple<Heightmap, Position, Position> {
                val heightmap = input.lines().map(String::toCharArray).toTypedArray().let(::Heightmap)
                val current = heightmap.positionsOf('S').single()
                heightmap.grid[current.y][current.x] = 'a'
                val bestSignal = heightmap.positionsOf('E').single()
                heightmap.grid[bestSignal.y][bestSignal.x] = 'z'
                return Triple(heightmap, current, bestSignal)
            }
        }
    }

    private fun Heightmap.positionsOf(elevation: Char) = sequence {
        for (y in 0..<ySize) {
            for (x in 0..<xSize) {
                if (get(x, y) == elevation) yield(Position(x, y))
            }
        }
    }

    private fun Heightmap.fewestStepsWithBFS(start: Position, end: Position): Int? {
        val visited = Array(ySize) { BooleanArray(xSize) }
        operator fun Array<BooleanArray>.get(x: Int, y: Int) = this[y][x]
        operator fun Array<BooleanArray>.set(x: Int, y: Int, value: Boolean) {
            this[y][x] = value
        }
        visited[start.x, start.y] = true

        data class Path(val x: Int, val y: Int, val steps: Int)

        val queue = ArrayDeque<Path>()
        var currentPath: Path? = Path(start.x, start.y, steps = 0)
        while (currentPath != null) {
            val (x, y, steps) = currentPath
            if (x == end.x && y == end.y) return steps

            val elevation = this[x, y]
            val nextSteps = steps + 1

            fun addAdjacentToQueue(x: Int, y: Int) {
                if (!visited[x, y] && this[x, y] - elevation <= 1) {
                    visited[x, y] = true
                    queue.addLast(Path(x, y, nextSteps))
                }
            }

            if (x > 0) addAdjacentToQueue(x = x - 1, y)
            if (x < xSize - 1) addAdjacentToQueue(x = x + 1, y)
            if (y > 0) addAdjacentToQueue(x, y = y - 1)
            if (y < ySize - 1) addAdjacentToQueue(x, y = y + 1)

            currentPath = queue.removeFirstOrNull()
        }

        return null
    }

    override fun part1(input: String): Int {
        val (heightmap, current, bestSignal) = Heightmap.parse(input)
        return heightmap.fewestStepsWithBFS(current, bestSignal)
            ?: error("There is no path from current position to location with best signal")
    }

    override fun part2(input: String): Int {
        // more efficient way would be to search from bestSignal to nearest 'a' instead of from all 'a's to bestSignal
        val (heightmap, _, bestSignal) = Heightmap.parse(input)
        return heightmap
            .positionsOf('a')
            .mapNotNull { position -> heightmap.fewestStepsWithBFS(position, bestSignal) }
            .min()
    }
}
