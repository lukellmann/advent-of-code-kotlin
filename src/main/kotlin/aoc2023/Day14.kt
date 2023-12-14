package aoc2023

import AoCDay

// https://adventofcode.com/2023/day/14
object Day14 : AoCDay<Int>(
    title = "Parabolic Reflector Dish",
    part1ExampleAnswer = 136,
    part1Answer = 106378,
    part2ExampleAnswer = 64,
    part2Answer = 90795,
) {
    private fun List<String>.tiltNorth(): List<String> {
        val p = map { it.toCharArray() }
        for ((rowIdx, row) in p.withIndex()) {
            for ((colIdx, char) in row.withIndex()) {
                if (char == '.') {
                    val replace = ((rowIdx + 1)..<p.size)
                        .takeWhile { p[it][colIdx] != '#' }
                        .firstOrNull { p[it][colIdx] == 'O' }
                    if (replace != null) {
                        row[colIdx] = 'O'
                        p[replace][colIdx] = '.'
                    }
                }
            }
        }
        return p.map(CharArray::concatToString)
    }

    private fun List<String>.tiltSouth() = reversed().tiltNorth().reversed()

    private fun List<String>.tiltWest() = map {
        val row = it.toCharArray()
        for ((index, char) in row.withIndex()) {
            if (char == '.') {
                val replace = ((index + 1)..<row.size)
                    .takeWhile { i -> row[i] != '#' }
                    .firstOrNull { i -> row[i] == 'O' }
                if (replace != null) {
                    row[index] = 'O'
                    row[replace] = '.'
                }
            }
        }
        row.concatToString()
    }

    private fun List<String>.tiltEast() = map(String::reversed).tiltWest().map(String::reversed)

    private fun List<String>.cycle() = tiltNorth().tiltWest().tiltSouth().tiltEast()

    private val List<String>.northSupportBeamLoad
        get() = reversed().withIndex().sumOf { (index, row) -> row.count('O'::equals) * (index + 1) }

    override fun part1(input: String) = input.lines().tiltNorth().northSupportBeamLoad

    override fun part2(input: String): Int {
        val platform = input.lines()
        val seen = linkedSetOf(platform)
        val (duplicate, _) = generateSequence(platform.cycle()) { it.cycle() }
            .map { p -> Pair(p, p in seen) }
            .onEach { (p, _) -> seen.add(p) }
            .first { (_, duplicate) -> duplicate }
        val repetitionLength = seen.size - seen.indexOf(duplicate)
        val nonRepetitionStart = seen.size - repetitionLength
        val missingCycles = (1_000_000_000 - nonRepetitionStart) % repetitionLength
        return generateSequence(duplicate.cycle()) { it.cycle() }.take(missingCycles).last().northSupportBeamLoad
    }
}
