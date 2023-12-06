package aoc2023

import AoCDay
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

// https://adventofcode.com/2023/day/6
object Day06 : AoCDay<Long>(
    title = "Wait For It",
    part1ExampleAnswer = 288,
    part1Answer = 1660968,
    part2ExampleAnswer = 71503,
    part2Answer = 26499773,
) {
    private class Race(val time: Long, val distanceRecord: Long)

    private fun parseRaces(input: String, ignoreSpaces: Boolean): List<Race> {
        fun String.parse() =
            if (ignoreSpaces) listOf(filterNot(' '::equals).toLong())
            else split(' ').filter(String::isNotBlank).map(String::toLong)
        val (times, distances) = input.split('\n', limit = 2)
        return times.substringAfter("Time:").parse().zip(distances.substringAfter("Distance:").parse())
            .map { (time, distance) -> Race(time, distance) }
    }

    private fun Race.calculateNumberOfWaysToBeatRecord(): Long {
        // c: charge time, t: time, d: distanceRecord
        // c(t - c) > d  <=>  ^2 - ct < -d  <=>  c^2 - ct + (t/2)^2 < (t/2)^2 - d  <=>  (c - t/2)^2 < (t/2)^2 - d
        // =>  c e ]t/2 - sqrt((t/2)^2 - d), t/2 + sqrt((t/2)^2 - d)[
        val t2 = time / 2.0
        val sqrt = sqrt(t2 * t2 - distanceRecord)
        val minReal = t2 - sqrt
        val maxReal = t2 + sqrt
        val min = ceil(minReal).let { if (it == minReal) it + 1 else it }.toLong()
        val max = floor(maxReal).let { if (it == maxReal) it - 1 else it }.toLong()
        return max - min + 1 // e.g. 2..5: 5 - 2 + 1 = 4
    }

    override fun part1(input: String) = parseRaces(input, ignoreSpaces = false)
        .map { it.calculateNumberOfWaysToBeatRecord() }
        .reduce(Long::times)

    override fun part2(input: String) = parseRaces(input, ignoreSpaces = true)
        .single()
        .calculateNumberOfWaysToBeatRecord()
}
