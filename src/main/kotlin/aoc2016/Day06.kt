package aoc2016

import AoCDay

// https://adventofcode.com/2016/day/6
object Day06 : AoCDay<String>(
    title = "Signals and Noise",
    part1ExampleAnswer = "easter",
    part1Answer = "nabgqlcw",
    part2ExampleAnswer = "advent",
    part2Answer = "ovtrjcjh",
) {
    private fun getErrorCorrectedMessage(input: String, useModifiedRepetitionCode: Boolean): String {
        val lines = input.lineSequence().iterator()
        val first = lines.next()
        val counts = List(first.length) { i -> hashMapOf(first[i] to 1) }
        for (line in lines) {
            for ((index, char) in line.withIndex()) {
                val count = counts[index]
                count[char] = (count[char] ?: 0) + 1
            }
        }
        return counts
            .map { count ->
                (if (useModifiedRepetitionCode) count.minBy { it.value } else count.maxBy { it.value }).key
            }
            .joinToString(separator = "")
    }

    override fun part1(input: String) = getErrorCorrectedMessage(input, useModifiedRepetitionCode = false)

    override fun part2(input: String) = getErrorCorrectedMessage(input, useModifiedRepetitionCode = true)
}
