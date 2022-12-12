package aoc2015

import AoCDay

// https://adventofcode.com/2015/day/16
object Day16 : AoCDay<Int>(
    title = "Aunt Sue",
    part1Answer = 40,
    part2Answer = 241,
) {
    private class Sue(val number: Int, val things: Map<String, Int>)

    private fun parseSues(input: String) = input
        .lineSequence()
        .map { line ->
            val (sue, things) = line.split(": ", limit = 2)
            Sue(
                number = sue.removePrefix("Sue ").toInt(),
                things = things
                    .split(", ")
                    .associate { thing ->
                        val (name, count) = thing.split(": ", limit = 2)
                        name to count.toInt()
                    },
            )
        }

    private val MFCSAM_COMPOUNDS_PART1 = listOf(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1,
    )

    override fun part1(input: String) = parseSues(input)
        .filter { sue ->
            MFCSAM_COMPOUNDS_PART1.fold(initial = true) { acc, (name, count) ->
                acc && (sue.things[name]?.let { it == count } ?: true)
            }
        }
        .single()
        .number

    private val MFCSAM_COMPOUNDS_PART2 = listOf<Pair<String, (Int) -> Boolean>>(
        "children" to { it == 3 },
        "cats" to { it > 7 },
        "samoyeds" to { it == 2 },
        "pomeranians" to { it < 3 },
        "akitas" to { it == 0 },
        "vizslas" to { it == 0 },
        "goldfish" to { it < 5 },
        "trees" to { it > 3 },
        "cars" to { it == 2 },
        "perfumes" to { it == 1 },
    )

    override fun part2(input: String) = parseSues(input)
        .filter { sue ->
            MFCSAM_COMPOUNDS_PART2.fold(initial = true) { acc, (name, predicate) ->
                acc && (sue.things[name]?.let(predicate) ?: true)
            }
        }
        .single()
        .number
}
