package aoc2024

import AoCDay
import util.isSorted

private typealias PageOrdering = Comparator<Int>
private typealias Update = List<Int>

// https://adventofcode.com/2024/day/5
object Day05 : AoCDay<Int>(
    title = "Print Queue",
    part1ExampleAnswer = 143,
    part1Answer = 4281,
    part2ExampleAnswer = 123,
    part2Answer = 5466,
) {
    private fun parsePageOrderingAndUpdates(input: String): Pair<PageOrdering, Sequence<Update>> {
        val (pageOrderingRules, updatePageNumbers) = input.split("\n\n", limit = 2)
        val pageSuccessors = pageOrderingRules
            .lineSequence()
            .groupBy(
                keySelector = { pageOrderingRule -> pageOrderingRule.substringBefore('|').toInt() },
                valueTransform = { pageOrderingRule -> pageOrderingRule.substringAfter('|').toInt() },
            )
        val pageOrdering = PageOrdering { pageNumber1, pageNumber2 ->
            when {
                pageNumber1 in (pageSuccessors[pageNumber2] ?: emptyList()) -> 1 // pn1 > pn2
                pageNumber2 in (pageSuccessors[pageNumber1] ?: emptyList()) -> -1// pn1 < pn2
                else -> throw IllegalArgumentException(
                    "no page ordering rule exists between page numbers $pageNumber1 and $pageNumber2"
                )
            }
        }
        val updates = updatePageNumbers
            .lineSequence()
            .map { update -> update.split(',').map(String::toInt) }
        return Pair(pageOrdering, updates)
    }

    private val Update.middlePageNumber get() = this[size / 2]

    override fun part1(input: String): Int {
        val (pageOrdering, updates) = parsePageOrderingAndUpdates(input)
        return updates
            .filter { update -> update.isSorted(pageOrdering) }
            .sumOf { it.middlePageNumber }
    }

    override fun part2(input: String): Int {
        val (pageOrdering, updates) = parsePageOrderingAndUpdates(input)
        return updates
            .filterNot { update -> update.isSorted(pageOrdering) }
            .map { update -> update.sortedWith(pageOrdering) }
            .sumOf { it.middlePageNumber }
    }
}
