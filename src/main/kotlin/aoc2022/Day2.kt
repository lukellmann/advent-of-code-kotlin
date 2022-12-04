package aoc2022

import AoCDay
import aoc2022.Day2.Shape.*

// https://adventofcode.com/2022/day/2
object Day2 : AoCDay(
    title = "Rock Paper Scissors",
    part1ExampleAnswer = 15,
    part1Answer = 13809,
    part2ExampleAnswer = 12,
    part2Answer = 12316,
) {
    private enum class Shape(val defeats: () -> Shape, val isDefeatedBy: () -> Shape, val score: Int) {
        ROCK(defeats = { SCISSORS }, isDefeatedBy = { PAPER }, score = 1),
        PAPER(defeats = { ROCK }, isDefeatedBy = { SCISSORS }, score = 2),
        SCISSORS(defeats = { PAPER }, isDefeatedBy = { ROCK }, score = 3),
    }

    private fun illegalChar(char: Char): Nothing = throw IllegalArgumentException("Illegal char: $char")

    private fun Char.toShape() = when (this) {
        'A' -> ROCK
        'B' -> PAPER
        'C' -> SCISSORS
        else -> illegalChar(this)
    }

    private fun calculateRoundScore(myShape: Shape, opponentShape: Shape): Int {
        val outcome = when {
            myShape == opponentShape -> 3 // it's a draw
            myShape.defeats() == opponentShape -> 6 // I won
            else -> 0 // I lost
        }
        return myShape.score + outcome
    }

    private fun sumRoundScores(input: String, selectMyShape: (opponentShape: Shape, column2: Char) -> Shape) = input
        .lineSequence()
        .map { line ->
            val opponentShape = line[0].toShape()
            val myShape = selectMyShape(opponentShape, line[2])

            calculateRoundScore(myShape, opponentShape)
        }
        .sum()

    override fun part1(input: String) = sumRoundScores(
        input,
        selectMyShape = { _, column2 ->
            when (column2) {
                'X' -> ROCK
                'Y' -> PAPER
                'Z' -> SCISSORS
                else -> illegalChar(column2)
            }
        },
    )

    override fun part2(input: String) = sumRoundScores(
        input,
        selectMyShape = { opponentShape, column2 ->
            when (column2) {
                'X' -> opponentShape.defeats() // I need to loose
                'Y' -> opponentShape // I need the round to end in a draw
                'Z' -> opponentShape.isDefeatedBy() // I need to win
                else -> illegalChar(column2)
            }
        },
    )
}
