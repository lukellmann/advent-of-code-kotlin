package aoc2023

import AoCDay
import aoc2023.Day07.Hand.Type.*
import util.illegalInput
import util.pow
import kotlin.math.max

// https://adventofcode.com/2023/day/7
object Day07 : AoCDay<Int>(
    title = "Camel Cards",
    part1ExampleAnswer = 6440,
    part1Answer = 250951660,
    part2ExampleAnswer = 5905,
    part2Answer = 251481660,
) {
    private const val CARDS = "AKQJT98765432"

    private class Hand(val cards: String) {
        init {
            if (cards.length != 5 || !cards.all(CARDS::contains)) illegalInput(cards)
        }

        enum class Type { HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, FULL_HOUSE, FOUR_OF_A_KIND, FIVE_OF_A_KIND }
    }

    private fun parseHandAndBid(input: String): Pair<Hand, Int> {
        val (hand, bid) = input.split(' ', limit = 2)
        return Pair(Hand(hand), bid.toInt())
    }

    private val POWS = run {
        val base = max(Hand.Type.entries.size, CARDS.length)
        List(6) { i -> base.pow(i) }
    }

    private inline fun Hand.strength(type: Hand.() -> Hand.Type, cardStrengths: String) = type().ordinal * POWS[5] +
        cards.foldIndexed(0) { index, acc, card -> acc + cardStrengths.indexOf(card) * POWS[4 - index] }

    private fun calculateTotalWinnings(input: String, cardStrengths: String, handType: (Hand) -> Hand.Type) = input
        .lineSequence()
        .map(::parseHandAndBid)
        .map { (hand, bid) -> Pair(hand.strength(handType, cardStrengths), bid) }
        .sortedBy { (handStrength, _) -> handStrength }
        .foldIndexed(0) { index, acc, (_, bid) -> acc + (index + 1) * bid }

    private fun handType(hand: Hand): Hand.Type {
        val counts = hand.cards.groupingBy { it }.eachCount()
        return when (counts.size) {
            1 -> FIVE_OF_A_KIND
            2 -> if (4 in counts.values) FOUR_OF_A_KIND else FULL_HOUSE
            3 -> if (3 in counts.values) THREE_OF_A_KIND else TWO_PAIR
            4 -> ONE_PAIR
            else -> HIGH_CARD
        }
    }

    private fun handTypeWithJokers(hand: Hand): Hand.Type {
        val (jokers, nonJokers) = hand.cards.partition('J'::equals)
        val nonJokerCounts = nonJokers.groupingBy { it }.eachCount()
        return when (jokers.length) {
            0 -> handType(hand)
            1 -> when (nonJokerCounts.size) {
                1 -> FIVE_OF_A_KIND
                2 -> if (3 in nonJokerCounts.values) FOUR_OF_A_KIND else FULL_HOUSE
                3 -> THREE_OF_A_KIND
                else -> ONE_PAIR
            }
            2 -> when (nonJokerCounts.size) {
                1 -> FIVE_OF_A_KIND
                2 -> FOUR_OF_A_KIND
                else -> THREE_OF_A_KIND
            }
            3 -> if (nonJokerCounts.size == 1) FIVE_OF_A_KIND else FOUR_OF_A_KIND
            else -> FIVE_OF_A_KIND
        }
    }

    override fun part1(input: String) = calculateTotalWinnings(input, cardStrengths = "23456789TJQKA", ::handType)

    override fun part2(input: String) =
        calculateTotalWinnings(input, cardStrengths = "J23456789TQKA", ::handTypeWithJokers)
}
