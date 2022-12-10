package aoc2015

import AoCDay
import aoc2015.Day13.Subject.Guest
import aoc2015.Day13.Subject.Me
import util.illegalInput
import util.match
import util.permutations

// https://adventofcode.com/2015/day/13
object Day13 : AoCDay<Int>(
    title = "Knights of the Dinner Table",
    part1ExampleAnswer = 330,
    part1Answer = 733,
    part2Answer = 725,
) {
    private sealed interface Subject {
        data class Guest(val name: String) : Subject
        object Me : Subject
    }

    private class HappinessList(private val map: Map<Guest, Map<Guest, Int>>) {
        val subjects = map.keys.toList()
        operator fun get(subject: Subject, neighbor: Subject) =
            if (subject == Me || neighbor == Me) 0 else map[subject]!![neighbor]!!
    }

    private val HAPPINESS_REGEX = Regex("""(\w+) would (lose|gain) (\d+) happiness units by sitting next to (\w+)\.""")

    private fun parseHappinessList(list: String): HappinessList {
        data class Happiness(val subject: Guest, val neighbor: Guest, val amount: Int)
        return list
            .lineSequence()
            .map { line -> HAPPINESS_REGEX.match(line) }
            .map { (subject, type, amount, neighbor) ->
                when (type) {
                    "lose" -> Happiness(Guest(subject), Guest(neighbor), -amount.toInt())
                    "gain" -> Happiness(Guest(subject), Guest(neighbor), +amount.toInt())
                    else -> illegalInput(type)
                }
            }
            .groupingBy { it.subject }
            .fold(initialValue = emptyMap<Guest, Int>()) { map, (_, neighbor, amount) -> map + (neighbor to amount) }
            .let(::HappinessList)
    }

    private fun totalChangeInHappinessForOptimalArrangement(input: String, includingMe: Boolean): Int {
        val happinessList = parseHappinessList(input)
        val subjects = if (includingMe) happinessList.subjects + Me else happinessList.subjects
        return subjects
            .permutations()
            .maxOf { arrangement ->
                val first = arrangement.first()
                val last = arrangement.last()
                arrangement
                    .zipWithNext { a, b -> happinessList[a, b] + happinessList[b, a] }.sum() +
                    happinessList[first, last] + happinessList[last, first]
            }
    }

    override fun part1(input: String) = totalChangeInHappinessForOptimalArrangement(input, includingMe = false)

    override fun part2(input: String) = totalChangeInHappinessForOptimalArrangement(input, includingMe = true)
}
