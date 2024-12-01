package aoc2024

import AoCDay
import kotlin.math.abs

// https://adventofcode.com/2024/day/1
object Day01 : AoCDay<Int>(
    title = "Historian Hysteria",
    part1ExampleAnswer = 11,
    part1Answer = 2031679,
    part2ExampleAnswer = 31,
    part2Answer = 19678534,
) {
    private fun parseLocationIdLists(input: String) = input
        .lineSequence()
        .map { line ->
            val (left, right) = line.split("   ", limit = 2)
            Pair(left.toInt(), right.toInt())
        }
        .toList()

    override fun part1(input: String): Int {
        val locationIdLists = parseLocationIdLists(input)
        val leftList = locationIdLists.map { (left, _) -> left }.sorted()
        val rightList = locationIdLists.map { (_, right) -> right }.sorted()
        return leftList.zip(rightList).sumOf { (left, right) -> abs(left - right) }
    }

    override fun part2(input: String): Int {
        val locationIdLists = parseLocationIdLists(input)
        val rightListFrequency = locationIdLists.groupingBy { (_, right) -> right }.eachCount()
        return locationIdLists.sumOf { (left, _) -> left * (rightListFrequency[left] ?: 0) }
    }
}
