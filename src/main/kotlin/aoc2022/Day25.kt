package aoc2022

import AoCDay
import util.illegalInput
import kotlin.math.abs

// https://adventofcode.com/2022/day/25
object Day25 : AoCDay<String>(
    title = "Full of Hot Air",
    part1ExampleAnswer = "2=-1=0",
    part1Answer = "2=112--220-=-00=-=20",
) {
    private fun Long.pow(n: Int): Long {
        var result = 1L
        repeat(n) { result *= this }
        return result
    }

    private fun CharSequence.toLong() = withIndex().sumOf { (index, digit) ->
        val value = when (digit) {
            '2' -> 2L
            '1' -> 1L
            '0' -> 0L
            '-' -> -1L
            '=' -> -2L
            else -> illegalInput(digit)
        }
        value * 5L.pow(lastIndex - index)
    }

    private val SNAFU_DIGITS = charArrayOf('2', '1', '0', '-', '=')

    private fun Long.toSnafu(): String {
        val snafu = StringBuilder()
        for (len in 1..Int.MAX_VALUE) {
            repeat(len - 1) { index -> snafu[index] = '0' }
            snafu.append('0')
            for (index in 0..<len) {
                val digit = SNAFU_DIGITS.minBy { digit ->
                    snafu[index] = digit
                    abs(snafu.toLong() - this)
                }
                snafu[index] = digit
                if (snafu.toLong() == this) return snafu.toString()
            }
        }
        error("Ran out of Int range")
    }

    override fun part1(input: String) = input.lineSequence().sumOf { snafu -> snafu.toLong() }.toSnafu()
}
