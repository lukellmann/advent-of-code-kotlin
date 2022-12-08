package aoc2015

import AoCDay

// https://adventofcode.com/2015/day/10
object Day10 : AoCDay<Int>(
    title = "Elves Look, Elves Say",
    part1Answer = 252594,
    part2Answer = 3579328,
) {
    private fun lookAndSaySequence(seed: String) = generateSequence(seed) { prev ->
        buildString(capacity = prev.length) {
            var char = prev[0]
            var charCount = 1
            for (i in 1..prev.lastIndex) {
                val c = prev[i]
                if (c == char) {
                    charCount++
                } else {
                    append(charCount).append(char)
                    char = c
                    charCount = 1
                }
            }
            append(charCount).append(char)
        }
    }

    override fun part1(input: String) = lookAndSaySequence(seed = input).take(41).last().length

    override fun part2(input: String) = lookAndSaySequence(seed = input).take(51).last().length
}
