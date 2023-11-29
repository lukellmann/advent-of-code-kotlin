package aoc2016

import AoCDay
import util.component1
import util.component2
import util.component3
import util.component4

// https://adventofcode.com/2016/day/7
object Day07 : AoCDay<Int>(
    title = "Internet Protocol Version 7",
    part1ExampleAnswer = 2,
    part1Answer = 115,
    part2ExampleAnswer = 3,
    part2Answer = 231,
) {
    private class IPv7(val supernetSequences: List<String>, val hypernetSequences: List<String>)

    private fun parseIPv7(input: String): IPv7 {
        val buf = StringBuilder()
        var inHypernetSequence = false
        val supernetSequences = mutableListOf<String>()
        val hypernetSequences = mutableListOf<String>()
        for (char in input) {
            when (char) {
                '[' -> {
                    require(!inHypernetSequence) { "Hypernet sequence must be closed before the next is opened" }
                    if (buf.isNotEmpty()) {
                        supernetSequences.add(buf.toString())
                        buf.clear()
                    }
                    inHypernetSequence = true
                }
                ']' -> {
                    require(inHypernetSequence) { "Hypernet sequence must be opened before it can be closed" }
                    hypernetSequences.add(buf.toString()) // also add empty ones
                    buf.clear()
                    inHypernetSequence = false
                }
                else -> buf.append(char)
            }
        }
        require(!inHypernetSequence) { "Hypernet sequence must be closed" }
        if (buf.isNotEmpty()) supernetSequences.add(buf.toString())
        return IPv7(supernetSequences, hypernetSequences)
    }

    private val String.containsAbba
        get() = windowedSequence(size = 4).any { (c1, c2, c3, c4) -> c1 != c2 && c1 == c4 && c2 == c3 }

    private val IPv7.supportsTls
        get() = supernetSequences.any { it.containsAbba } && hypernetSequences.none { it.containsAbba }

    private inline fun List<String>.forEachAba(action: (Char, Char) -> Unit) {
        for (string in this) {
            for ((c1, c2, c3) in string.windowedSequence(size = 3)) {
                if (c1 != c2 && c1 == c3) action(c1, c2)
            }
        }
    }

    private val IPv7.supportsSsl: Boolean
        get() {
            supernetSequences.forEachAba { s1, s2 ->
                hypernetSequences.forEachAba { h1, h2 ->
                    if (s1 == h2 && s2 == h1) return true
                }
            }
            return false
        }

    override fun part1(input: String) = input
        .lineSequence()
        .map(::parseIPv7)
        .count { it.supportsTls }

    override fun part2(input: String) = input
        .lineSequence()
        .map(::parseIPv7)
        .count { it.supportsSsl }
}
