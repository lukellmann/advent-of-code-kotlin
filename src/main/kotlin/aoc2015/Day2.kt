package aoc2015

import AoCDay

// https://adventofcode.com/2015/day/2
object Day2 : AoCDay<Int>(
    title = "I Was Told There Would Be No Math",
    part1ExampleAnswer = 58 + 43,
    part1Answer = 1586300,
    part2ExampleAnswer = 34 + 14,
    part2Answer = 3737498,
) {
    private fun dimensionsSequence(input: String) = input
        .lineSequence()
        .map { line -> line.split('x').map(String::toInt) }

    override fun part1(input: String) = dimensionsSequence(input).sumOf { (l, w, h) ->
        val s1 = l * w
        val s2 = w * h
        val s3 = h * l
        2 * s1 + 2 * s2 + 2 * s3 + minOf(s1, s2, s3)
    }

    override fun part2(input: String) = dimensionsSequence(input).sumOf { (l, w, h) ->
        val sides = 2 * l + 2 * w + 2 * h - 2 * maxOf(l, w, h)
        val bow = l * w * h
        sides + bow
    }
}
