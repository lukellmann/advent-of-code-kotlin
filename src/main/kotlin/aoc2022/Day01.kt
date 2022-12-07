package aoc2022

import AoCDay

// https://adventofcode.com/2022/day/1
object Day01 : AoCDay<Int>(
    title = "Calorie Counting",
    part1ExampleAnswer = 24000,
    part1Answer = 70698,
    part2ExampleAnswer = 45000,
    part2Answer = 206643,
) {
    private fun caloriesCarriedByElves(input: String) = input
        .splitToSequence("\n\n")
        .map { elfInventory -> elfInventory.lineSequence().sumOf(String::toInt) }

    override fun part1(input: String) = caloriesCarriedByElves(input).max()

    override fun part2(input: String) = caloriesCarriedByElves(input).sortedDescending().take(3).sum()
}
