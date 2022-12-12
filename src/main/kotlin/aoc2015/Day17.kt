package aoc2015

import AoCDay

// https://adventofcode.com/2015/day/17
object Day17 : AoCDay<Int>(
    title = "No Such Thing as Too Much",
    part1Answer = 654,
    part2Answer = 57,
) {
    private fun parseContainers(input: String) = input.lines().map(String::toInt).toIntArray()

    override fun part1(input: String): Int {
        val containers = parseContainers(input)
        require(containers.size < 31) { "Bit-trick won't work with containers >= 31" }
        var combinations = 0
        outer@ for (combination in 0..<(1 shl containers.size)) {
            var total = 0
            for (index in containers.indices) {
                if ((1 shl index) and combination != 0) {
                    total += containers[index]
                    if (total > 150) continue@outer
                }
            }
            if (total == 150) combinations++
        }
        return combinations
    }

    override fun part2(input: String): Int {
        val containers = parseContainers(input)
        require(containers.size < 31) { "Bit-trick won't work with containers >= 31" }
        val combinations = mutableListOf<Int>()
        outer@ for (combination in 0..<(1 shl containers.size)) {
            var total = 0
            var n = 0
            for (index in containers.indices) {
                if ((1 shl index) and combination != 0) {
                    total += containers[index]
                    n++
                    if (total > 150) continue@outer
                }
            }
            if (total == 150) combinations += n
        }
        val min = combinations.min()
        return combinations.count { it == min }
    }
}
