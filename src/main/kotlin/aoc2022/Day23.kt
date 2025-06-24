package aoc2022

import AoCDay
import util.illegalInput

// https://adventofcode.com/2022/day/23
object Day23 : AoCDay<Int>(
    title = "Unstable Diffusion",
    part1ExampleAnswer = 110,
    part1Answer = 4146,
    part2ExampleAnswer = 20,
    part2Answer = 957,
) {
    private data class Position(val x: Int, val y: Int)

    private fun parseElfPositions(input: String) = buildSet {
        for ((y, line) in input.lineSequence().withIndex()) {
            for ((x, char) in line.withIndex()) {
                when (char) {
                    '.' -> {}
                    '#' -> add(Position(x, y))
                    else -> illegalInput(char)
                }
            }
        }
    }

    private data class RoundResult(val round: Int, val before: Set<Position>, val after: Set<Position>)
    private enum class Direction { NORTH, SOUTH, WEST, EAST }

    private inline fun simulateRoundsUntil(input: String, stopPredicate: (RoundResult) -> Boolean): RoundResult {
        var elves = parseElfPositions(input)
        val directions = ArrayDeque(Direction.entries)
        for (round in 1..Int.MAX_VALUE) {
            val proposedPositions = elves.associateWith { elf ->
                val (x, y) = elf
                val hasAdjacentElf = (-1..1).any { dx ->
                    (-1..1).any { dy -> (dx != 0 || dy != 0) && Position(x + dx, y + dy) in elves }
                }
                if (hasAdjacentElf) {
                    directions
                        .firstOrNull { direction ->
                            when (direction) {
                                NORTH -> (-1..1).all { dx -> Position(x + dx, y - 1) !in elves }
                                SOUTH -> (-1..1).all { dx -> Position(x + dx, y + 1) !in elves }
                                WEST -> (-1..1).all { dy -> Position(x - 1, y + dy) !in elves }
                                EAST -> (-1..1).all { dy -> Position(x + 1, y + dy) !in elves }
                            }
                        }
                        ?.let { direction ->
                            when (direction) {
                                NORTH -> Position(x, y - 1)
                                SOUTH -> Position(x, y + 1)
                                WEST -> Position(x - 1, y)
                                EAST -> Position(x + 1, y)
                            }
                        }
                        ?: elf
                } else elf
            }
            val proposedCounts = proposedPositions.values.groupingBy { it }.eachCount()
            val movedElves = proposedPositions
                .map { (elf, proposed) -> if (proposedCounts[proposed] == 1) proposed else elf }
                .toSet()
            val roundResult = RoundResult(round, before = elves, after = movedElves)
            if (stopPredicate(roundResult)) return roundResult
            elves = movedElves
            directions.addLast(directions.removeFirst())
        }
        error("Round ran out of Int range")
    }

    override fun part1(input: String): Int {
        val (_, _, after) = simulateRoundsUntil(input) { (round, _, _) -> round == 10 }
        val rectangleArea = (after.maxOf { it.x } - after.minOf { it.x } + 1) *
            (after.maxOf { it.y } - after.minOf { it.y } + 1)
        return rectangleArea - after.size
    }

    override fun part2(input: String) = simulateRoundsUntil(input) { (_, before, after) -> before == after }.round
}
