package aoc2015

import AoCDay

// https://adventofcode.com/2015/day/19
object Day19 : AoCDay<Int>(
    title = "Medicine for Rudolph",
    part1ExampleAnswer = 4,
    part1Answer = 576,
    part2ExampleAnswer = null,
    part2Answer = null,
) {
    private data class Replacement(val from: String, val to: String)

    private fun parseReplacementsAndMolecule(input: String): Pair<List<Replacement>, String> {
        val (replacements, molecule) = input.split("\n\n", limit = 2)
        return Pair(
            first = replacements
                .lines()
                .map { replacement ->
                    val (from, to) = replacement.split(" => ", limit = 2)
                    Replacement(from, to)
                },
            second = molecule,
        )
    }

    override fun part1(input: String): Int {
        val (replacements, molecule) = parseReplacementsAndMolecule(input)
        val molecules = HashSet<String>()

        for ((from, to) in replacements) {
            var index = molecule.indexOf(from)
            while (index >= 0) {
                val endIndex = index + from.length
                molecules += molecule.replaceRange(startIndex = index, endIndex, replacement = to)
                index = molecule.indexOf(from, startIndex = endIndex)
            }
        }

        return molecules.size
    }

    // I gave up here for now
    // maybe I should give https://www.reddit.com/r/adventofcode/comments/3xflz8/day_19_solutions/
    // another look in the future when I feel like it...
    override fun part2(input: String) = 0
}
