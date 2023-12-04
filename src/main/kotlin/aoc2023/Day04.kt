package aoc2023

import AoCDay

// https://adventofcode.com/2023/day/4
object Day04 : AoCDay<Int>(
    title = "Scratchcards",
    part1ExampleAnswer = 13,
    part1Answer = 20829,
    part2ExampleAnswer = 30,
    part2Answer = 12648035,
) {
    private class Card(val id: Int, val winningNumbers: List<Int>, val myNumbers: List<Int>)

    private val SPACE = Regex("""\s+""")
    private fun parseCard(input: String): Card {
        val (card, numbers) = input.split(':', limit = 2)
        val id = card.substringAfter("Card").trim().toInt()
        val (winningNumbers, myNumbers) = numbers.split('|', limit = 2)
        fun parseNumbers(input: String) = input.trim().split(SPACE).map(String::toInt)
        return Card(id, parseNumbers(winningNumbers), parseNumbers(myNumbers))
    }

    private val Card.matchingNumbers get() = myNumbers.filter { it in winningNumbers }.size

    private val Card.points get() = matchingNumbers.let { if (it == 0) 0 else 1 shl (it - 1) }

    override fun part1(input: String) = input
        .lineSequence()
        .map(::parseCard)
        .sumOf { it.points }

    override fun part2(input: String): Int {
        val cards = input.lines().map(::parseCard)
        val copies = cards.associateTo(HashMap()) { it.id to 1 }
        for (card in cards) {
            val currentCopies = copies.getValue(card.id)
            repeat(card.matchingNumbers) { i ->
                val nextId = card.id + i + 1
                copies[nextId] = copies.getValue(nextId) + currentCopies
            }
        }
        return copies.values.sum()
    }
}
