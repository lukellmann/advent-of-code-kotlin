package aoc2023

import AoCDay
import kotlin.math.max
import kotlin.math.min

// https://adventofcode.com/2023/day/5
object Day05 : AoCDay<Long>(
    title = "If You Give A Seed A Fertilizer",
    part1ExampleAnswer = 35,
    part1Answer = 218513636,
    part2ExampleAnswer = 46,
    part2Answer = 81956384,
) {
    private class Mapping(val source: LongRange, val destinationShift: Long)
    private data class Almanac(val seeds: List<Long>, val maps: List<List<Mapping>>)

    private fun parseAlmanac(input: String): Almanac {
        val sections = input.split("\n\n")
        val seeds = sections.first().substringAfter("seeds: ").split(' ').map(String::toLong)
        val maps = sections.drop(1).map { map ->
            map.lines().drop(1).map { mapping ->
                val (destStart, srcStart, length) = mapping.split(' ', limit = 3).map(String::toLong)
                Mapping(source = srcStart..<(srcStart + length), destinationShift = destStart - srcStart)
            }.sortedBy { it.source.first } // applyMap expects maps to be sorted by their source range start
        }
        return Almanac(seeds, maps)
    }

    private infix fun LongRange.intersect(other: LongRange) = max(this.first, other.first)..min(this.last, other.last)
    private fun LongRange.shift(amount: Long) = (first + amount)..(last + amount)
    private fun LongRange.splitAround(other: LongRange) = Pair(this.first..<other.first, (other.last + 1)..this.last)
    private operator fun List<LongRange>.plus(element: LongRange) = plusElement(element)

    private fun List<LongRange>.normalize() = filterNot(LongRange::isEmpty)
        .sortedBy(LongRange::first)
        .fold(initial = emptyList<LongRange>()) { acc, cur ->
            val prev = acc.lastOrNull()
            when {
                prev == null -> listOf(cur)
                prev.last >= cur.first - 1 -> acc.dropLast(1) + (prev.first..max(prev.last, cur.last))
                else -> acc + cur
            }
        }

    private fun List<LongRange>.applyMap(map: List<Mapping>) = flatMap { range ->
        val (acc, pending) = map.fold(
            initial = Pair(emptyList<LongRange>(), range)
        ) { (acc, pending), mapping -> // mappings have to be sorted by their source range start
            val mappedNumbers = pending intersect mapping.source
            if (mappedNumbers.isEmpty()) Pair(acc, pending) else {
                val (smaller, bigger) = pending.splitAround(mappedNumbers)
                Pair(acc + smaller + mappedNumbers.shift(mapping.destinationShift), bigger)
            }
        }
        acc + pending
    }.normalize()

    private fun findLowestLocationNumber(seedRanges: List<LongRange>, maps: List<List<Mapping>>) =
        maps.fold(initial = seedRanges.normalize()) { acc, map -> acc.applyMap(map) }.first().first

    override fun part1(input: String): Long {
        val (seeds, maps) = parseAlmanac(input)
        val seedRanges = seeds.map { it..it } // just part2 with singleton ranges
        return findLowestLocationNumber(seedRanges, maps)
    }

    override fun part2(input: String): Long {
        val (seeds, maps) = parseAlmanac(input)
        val seedRanges = seeds.chunked(2).map { (start, length) -> start..<(start + length) }
        return findLowestLocationNumber(seedRanges, maps)
    }
}
