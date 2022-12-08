@file:Suppress("DuplicatedCode") // I know...

package aoc2022

import AoCDay
import kotlin.math.max

// https://adventofcode.com/2022/day/8
object Day08 : AoCDay<Int>(
    title = "Treetop Tree House",
    part1ExampleAnswer = 21,
    part1Answer = 1533,
    part2ExampleAnswer = 8,
    part2Answer = 345744,
) {
    private class Map(private val grid: String) {
        val xSize = grid.indexOf('\n')
        val ySize = (grid.length + 1) / (xSize + 1)
        operator fun get(x: Int, y: Int) = grid[(y * (xSize + 1)) + x]
        inline fun <T> foldInteriorTrees(initial: T, operation: (acc: T, tree: Char, x: Int, y: Int) -> T): T {
            var acc = initial
            for (y in 1..ySize - 2) {
                for (x in 1..xSize - 2) {
                    acc = operation(acc, get(x, y), x, y)
                }
            }
            return acc
        }
    }

    override fun part1(input: String): Int {
        val map = Map(input)
        return map.foldInteriorTrees(
            initial = map.xSize * 2 + (map.ySize - 2) * 2 // edges are always visible
        ) fold@{ visibleFromOutside, tree, x, y ->
            // run from x y into all directions while trees are smaller, if we run out of the map, the tree is visible
            var up = y - 1
            while (up >= 0 && map[x, up] < tree) up-- // note difference in comparison operator compared to part2
            if (up < 0) return@fold visibleFromOutside + 1
            var down = y + 1
            while (down < map.ySize && map[x, down] < tree) down++
            if (down >= map.ySize) return@fold visibleFromOutside + 1
            var left = x - 1
            while (left >= 0 && map[left, y] < tree) left--
            if (left < 0) return@fold visibleFromOutside + 1
            var right = x + 1
            while (right < map.xSize && map[right, y] < tree) right++
            if (right >= map.xSize) visibleFromOutside + 1 else visibleFromOutside
        }
    }

    override fun part2(input: String): Int {
        val map = Map(input)
        return map.foldInteriorTrees(
            initial = 0 // scenic score for all edges is 0
        ) { maxScenicScore, tree, x, y ->
            // run from x y into all directions while trees are smaller, score can be calculated by subtracting indices
            var up = y - 1
            while (up > 0 && map[x, up] < tree) up-- // note difference in comparison operator compared to part1
            var down = y + 1
            while (down < map.ySize - 1 && map[x, down] < tree) down++
            var left = x - 1
            while (left > 0 && map[left, y] < tree) left--
            var right = x + 1
            while (right < map.xSize - 1 && map[right, y] < tree) right++
            max(maxScenicScore, (y - up) * (down - y) * (x - left) * (right - x))
        }
    }
}
