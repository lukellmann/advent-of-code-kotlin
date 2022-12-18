package aoc2022

import AoCDay
import kotlin.math.abs

// https://adventofcode.com/2022/day/18
object Day18 : AoCDay<Int>(
    title = "Boiling Boulders",
    part1ExampleAnswer = 64,
    part1Answer = 3396,
    part2ExampleAnswer = 58,
    part2Answer = 2044,
) {
    private data class Cube(val x: Int, val y: Int, val z: Int)

    private fun parseCubes(input: String) = input
        .lineSequence()
        .map { line -> line.split(',', limit = 3) }
        .map { (x, y, z) -> Cube(x.toInt(), y.toInt(), z.toInt()) }
        .toSet()

    private fun manhattanDistance(c1: Cube, c2: Cube) =
        abs(c1.x - c2.x) + abs(c1.y - c2.y) + abs(c1.z - c2.z)

    private infix fun Cube.isAdjacentTo(other: Cube) = manhattanDistance(this, other) == 1

    private const val CUBE_SIDES = 6

    override fun part1(input: String): Int {
        val cubes = parseCubes(input)
        return cubes.sumOf { c1 -> CUBE_SIDES - cubes.count { c2 -> c1 isAdjacentTo c2 } }
    }

    override fun part2(input: String): Int {
        val cubes = parseCubes(input)

        val minX = cubes.minOf { it.x }
        val maxX = cubes.maxOf { it.x }
        val minY = cubes.minOf { it.y }
        val maxY = cubes.maxOf { it.y }
        val minZ = cubes.minOf { it.z }
        val maxZ = cubes.maxOf { it.z }
        val grid = Array(maxX - minX + 1) { Array(maxY - minY + 1) { BooleanArray(maxZ - minZ + 1) } }
        for ((x, y, z) in cubes) {
            grid[x - minX][y - minY][z - minZ] = true
        }

        val xs = 1..<(maxX - minX)
        val ys = 1..<(maxY - minY)
        val zs = 1..<(maxZ - minZ)

        val airPocketMemory = HashMap<Cube, Boolean>()
        fun isAirPocketWithDFS(cube: Cube, visited: MutableSet<Cube>): Boolean {
            val (x, y, z) = cube
            if (x !in xs || y !in ys || z !in zs) return false

            fun searchNeighbor(x: Int, y: Int, z: Int): Boolean {
                if (grid[x][y][z]) return true
                val neighbor = Cube(x, y, z)
                return if (visited.add(neighbor)) {
                    airPocketMemory[neighbor]
                        ?: isAirPocketWithDFS(cube = neighbor, visited).also { airPocketMemory[neighbor] = it }
                } else true
            }

            return searchNeighbor(x - 1, y, z) && searchNeighbor(x + 1, y, z)
                && searchNeighbor(x, y - 1, z) && searchNeighbor(x, y + 1, z)
                && searchNeighbor(x, y, z - 1) && searchNeighbor(x, y, z + 1)
        }

        val airPockets = buildSet {
            for (x in xs) {
                for (y in ys) {
                    for (z in zs) {
                        if (grid[x][y][z]) continue
                        val cube = Cube(x, y, z)
                        if (isAirPocketWithDFS(cube, visited = hashSetOf(cube))) {
                            add(Cube(x + minX, y + minY, z + minZ))
                        }
                    }
                }
            }
        }

        return cubes.sumOf { c1 ->
            CUBE_SIDES - cubes.count { c2 -> c1 isAdjacentTo c2 } -
                airPockets.count { airPocket -> c1 isAdjacentTo airPocket }
        }
    }
}
