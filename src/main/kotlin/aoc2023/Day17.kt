package aoc2023

import AoCDay
import aoc2023.Day17.Dir.*
import util.Vec2
import util.plus
import java.util.*

// https://adventofcode.com/2023/day/17
object Day17 : AoCDay<Int>(
    title = "Clumsy Crucible",
    part1ExampleAnswer = 102,
    part1Answer = 953,
    part2ExampleAnswer = 94,
    part2Answer = 1180,
) {
    private enum class Dir(val vec: Vec2) {
        UP(Vec2(0, -1)),
        DOWN(Vec2(0, 1)),
        RIGHT(Vec2(1, 0)),
        LEFT(Vec2(-1, 0)),
    }

    private fun leastHeatLoss(input: String, minBeforeTurn: Int, maxConsecutive: Int): Int {
        class Path(val heatLoss: Int, val pos: Vec2, val dir: Dir, val consecutive: Int)

        fun Path.seenKey() = Triple(pos, dir, consecutive)

        val heatLossMap = input.lines().map { line -> line.map { it - '0' } }
        val destination = Vec2(x = heatLossMap.first().lastIndex, y = heatLossMap.lastIndex)
        val xs = 0..destination.x
        val ys = 0..destination.y
        val paths = PriorityQueue(compareBy(Path::heatLoss)).apply {
            add(Path(heatLoss = heatLossMap[0][1], pos = Vec2(1, 0), dir = RIGHT, consecutive = 1))
            add(Path(heatLoss = heatLossMap[1][0], pos = Vec2(0, 1), dir = DOWN, consecutive = 1))
        }
        val seen = HashSet(paths.map(Path::seenKey))
        while (true) {
            val path = paths.remove()
            if (path.pos == destination && path.consecutive >= minBeforeTurn) return path.heatLoss
            fun go(dir: Dir, consecutive: Int) {
                val pos = path.pos + dir.vec
                if (pos.x in xs && pos.y in ys) {
                    val p = Path(heatLoss = path.heatLoss + heatLossMap[pos.y][pos.x], pos, dir, consecutive)
                    if (seen.add(p.seenKey())) paths.add(p)
                }
            }
            if (path.consecutive < maxConsecutive) go(path.dir, consecutive = path.consecutive + 1)
            if (path.consecutive >= minBeforeTurn) when (path.dir) {
                UP, DOWN -> {
                    go(LEFT, consecutive = 1)
                    go(RIGHT, consecutive = 1)
                }
                RIGHT, LEFT -> {
                    go(UP, consecutive = 1)
                    go(DOWN, consecutive = 1)
                }
            }
        }
    }

    override fun part1(input: String) = leastHeatLoss(input, minBeforeTurn = 0, maxConsecutive = 3)

    override fun part2(input: String) = leastHeatLoss(input, minBeforeTurn = 4, maxConsecutive = 10)
}
