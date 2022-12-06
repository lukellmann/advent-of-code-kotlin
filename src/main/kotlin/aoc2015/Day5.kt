package aoc2015

import AoCDay
import util.component1
import util.component2
import util.component3

// https://adventofcode.com/2015/day/5
object Day5 : AoCDay<Int>(
    title = "Doesn't He Have Intern-Elves For This?",
    part1ExampleAnswer = 2,
    part1Answer = 236,
    part2ExampleAnswer = 2,
    part2Answer = 51,
) {
    private const val VOWELS = "aeiou"
    private val BAD_SUBSTRINGS = hashSetOf("ab", "cd", "pq", "xy")

    private inline fun countNiceStrings(input: String, isNice: (String) -> Boolean) = input.lineSequence().count(isNice)

    override fun part1(input: String) = countNiceStrings(input) isNice@{ string ->
        if (string.length < 3) return@isNice false

        var vowels = 0
        var doubleLetter = false
        for (substring in string.windowedSequence(size = 2, step = 1)) {
            if (substring in BAD_SUBSTRINGS) return@isNice false
            val (a, b) = substring
            if (a in VOWELS) vowels++
            if (a == b) doubleLetter = true
        }
        if (string.last() in VOWELS) vowels++

        return@isNice vowels >= 3 && doubleLetter
    }

    override fun part2(input: String) = countNiceStrings(input) isNice@{ string ->
        if (string.length < 4) return@isNice false

        var pairOfTwo = false
        var repeatsWithOneBetween = false
        var (a, b, c) = string
        for (i in 3..string.lastIndex) {
            if (a == c) repeatsWithOneBetween = true
            if (!pairOfTwo) for (j in i..string.lastIndex) {
                if (a == string[j - 1] && b == string[j]) {
                    pairOfTwo = true
                    break
                }
            }
            a = b; b = c; c = string[i]
        }
        if (a == c) repeatsWithOneBetween = true

        return@isNice pairOfTwo && repeatsWithOneBetween
    }
}
