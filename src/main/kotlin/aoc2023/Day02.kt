package aoc2023

import AoCDay
import kotlin.math.max

// https://adventofcode.com/2023/day/2
object Day02 : AoCDay<Int>(
    title = "Cube Conundrum",
    part1ExampleAnswer = 8,
    part1Answer = 2256,
    part2ExampleAnswer = 2286,
    part2Answer = 74229,
) {
    private class CubeSet(val red: Int, val green: Int, val blue: Int)
    private class Game(val id: Int, val rounds: List<CubeSet>)

    private fun parseCubeSet(input: String): CubeSet {
        val colors = input.split(", ", limit = 3).associate {
            val (number, color) = it.split(' ', limit = 2)
            color to number.toInt()
        }
        return CubeSet(colors["red"] ?: 0, colors["green"] ?: 0, colors["blue"] ?: 0)
    }

    private fun parseGame(input: String): Game {
        val (game, rounds) = input.split(": ", limit = 2)
        val id = game.substringAfter("Game ").toInt()
        return Game(id, rounds.split("; ").map(::parseCubeSet))
    }

    private val Game.isPossibleWith12Red13Green14Blue
        get() = rounds.all { it.red <= 12 && it.green <= 13 && it.blue <= 14 }

    private val Game.minimumCubeSetToMakePossible
        get() = rounds.fold(initial = CubeSet(0, 0, 0)) { acc, cur ->
            CubeSet(max(acc.red, cur.red), max(acc.green, cur.green), max(acc.blue, cur.blue))
        }

    private val CubeSet.power get() = red * green * blue

    override fun part1(input: String) = input
        .lineSequence()
        .map(::parseGame)
        .filter { it.isPossibleWith12Red13Green14Blue }
        .sumOf { it.id }

    override fun part2(input: String) = input
        .lineSequence()
        .map(::parseGame)
        .map { it.minimumCubeSetToMakePossible }
        .sumOf { it.power }
}
