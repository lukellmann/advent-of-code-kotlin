package aoc2022

import AoCDay

// https://adventofcode.com/2022/day/4
object Day04 : AoCDay<Int>(
    title = "Camp Cleanup",
    part1ExampleAnswer = 2,
    part1Answer = 459,
    part2ExampleAnswer = 4,
    part2Answer = 779,
) {
    private class ElfPair(input: String) {
        val sections1: IntRange
        val sections2: IntRange

        init {
            val (s1, s2) = input.split(',')

            fun String.toSections() = split('-')
                .map(String::toInt)
                .let { (start, end) -> start..end }

            sections1 = s1.toSections()
            sections2 = s2.toSections()
        }
    }

    private fun elfPairs(input: String) = input.lineSequence().map(::ElfPair)

    private operator fun <T : Comparable<T>> ClosedRange<T>.contains(other: ClosedRange<T>) =
        other.isEmpty() || ((other.start >= this.start) && (other.endInclusive <= this.endInclusive))

    private infix fun <T : Comparable<T>> ClosedRange<T>.overlapsWith(other: ClosedRange<T>) =
        this.isEmpty() || other.isEmpty() ||
            (this.start in other) || (this.endInclusive in other) ||
            (other.start in this) || (other.endInclusive in this)

    override fun part1(input: String) = elfPairs(input)
        .count { it.sections1 in it.sections2 || it.sections2 in it.sections1 }

    override fun part2(input: String) = elfPairs(input)
        .count { it.sections1 overlapsWith it.sections2 }
}
