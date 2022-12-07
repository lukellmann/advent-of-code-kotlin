package aoc2022

import AoCDay
import util.illegalInput

// https://adventofcode.com/2022/day/3
object Day03 : AoCDay<Int>(
    title = "Rucksack Reorganization",
    part1ExampleAnswer = 157,
    part1Answer = 8139,
    part2ExampleAnswer = 70,
    part2Answer = 2668,
) {
    private class Rucksack(val items: String) {
        val compartment1 get() = items.substring(0, items.length / 2)
        val compartment2 get() = items.substring(items.length / 2, items.length)
    }

    private fun rucksacks(input: String) = input.lineSequence().map(::Rucksack)

    private fun Sequence<Char>.sumOfItemPriorities(): Int = sumOf { item ->
        when (item) {
            in 'a'..'z' -> (item - 'a') + 1
            in 'A'..'Z' -> (item - 'A') + 27
            else -> illegalInput(item)
        }
    }

    override fun part1(input: String) = rucksacks(input)
        .map { rucksack ->
            rucksack.compartment1.first { item -> item in rucksack.compartment2 }
        }
        .sumOfItemPriorities()

    override fun part2(input: String) = rucksacks(input)
        .chunked(3)
        .map { (r1, r2, r3) ->
            r1.items.first { item -> item in r2.items && item in r3.items }
        }
        .sumOfItemPriorities()
}
