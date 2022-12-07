package aoc2022

import AoCDay
import aoc2022.Day02.Shape.*
import util.component1
import util.component2
import util.component3
import util.illegalInput

// https://adventofcode.com/2022/day/2
object Day02 : AoCDay<Int>(
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

    private fun Char.toShape() = when (this) {
        'A' -> ROCK
        'B' -> PAPER
        'C' -> SCISSORS
        else -> illegalInput(this)
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
        .sumOf { (column1, _, column2) ->
            val opponentShape = column1.toShape()
            val myShape = selectMyShape(opponentShape, column2)

            calculateRoundScore(myShape, opponentShape)
        }

    override fun part1(input: String) = sumRoundScores(
        input,
        selectMyShape = { _, column2 ->
            when (column2) {
                'X' -> ROCK
                'Y' -> PAPER
                'Z' -> SCISSORS
                else -> illegalInput(column2)
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
                else -> illegalInput(column2)
            }
        },
    )
}
