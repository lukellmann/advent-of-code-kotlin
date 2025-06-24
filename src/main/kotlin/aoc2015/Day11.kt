package aoc2015

import AoCDay
import aoc2015.Day11.Password
import util.component1
import util.component2
import util.component3

// https://adventofcode.com/2015/day/11
object Day11 : AoCDay<Password>(
    title = "Corporate Policy",
    part1ExampleAnswer = "ghjaabcc",
    part1Answer = "cqjxxyzz",
    part2Answer = "cqkaabcc",
) {
    private typealias Password = String

    private fun nextPasswords(current: Password) = generateSequence(seed = current) { pw ->
        var indexBeforeZ = pw.lastIndex
        while (indexBeforeZ >= 0 && pw[indexBeforeZ] == 'z') indexBeforeZ--
        if (indexBeforeZ < 0) {
            "a".repeat(pw.length + 1)
        } else buildString(capacity = pw.length) {
            append(pw, 0, indexBeforeZ)
            append(pw[indexBeforeZ] + 1)
            repeat(pw.lastIndex - indexBeforeZ) { append('a') }
        }
    }.drop(1) // seed is included but we don't want it

    private val Password.isValid: Boolean
        get() = length == 8 &&
            all { char -> char in 'a'..'h' || char == 'j' || char == 'k' || char in 'm'..'z' } &&
            windowed(size = 3, step = 1).any { (a, b, c) -> a + 1 == b && b + 1 == c } &&
            run {
                var pairs = 0
                var index = 0
                while (index < lastIndex && pairs < 2) {
                    index += if (this[index] == this[index + 1]) {
                        pairs++
                        2
                    } else 1
                }
                pairs
            } >= 2

    override fun part1(input: Password) = nextPasswords(current = input).first { it.isValid }

    override fun part2(input: Password) = nextPasswords(current = input).filter { it.isValid }.elementAt(1)
}
