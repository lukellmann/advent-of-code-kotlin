package aoc2023

import AoCDay
import util.illegalInput

// https://adventofcode.com/2023/day/15
object Day15 : AoCDay<Int>(
    title = "Lens Library",
    part1ExampleAnswer = 1320,
    part1Answer = 495972,
    part2ExampleAnswer = 145,
    part2Answer = 245223,
) {
    private fun hash(string: String) = string.fold(initial = 0) { cur, char -> ((cur + char.code) * 17) and 0xFF }

    private sealed interface Step {
        val lensLabel: String

        class Remove(override val lensLabel: String) : Step
        class ReplaceOrAdd(override val lensLabel: String, val lensFocalLength: Int) : Step
    }

    private fun parseStep(input: String) = when (val last = input.last()) {
        '-' -> Step.Remove(lensLabel = input.dropLast(1))
        in '1'..'9' -> {
            if (input[input.length - 2] != '=') illegalInput(input)
            Step.ReplaceOrAdd(lensLabel = input.dropLast(2), lensFocalLength = last - '0')
        }
        else -> illegalInput(input)
    }

    override fun part1(input: String) = input.split(',').sumOf(::hash)

    override fun part2(input: String): Int {
        val boxes = List(256) { LinkedHashMap<String, Int>() }
        for (step in input.split(',').map(::parseStep)) {
            val box = boxes[hash(step.lensLabel)]
            when (step) {
                is Step.Remove -> box.remove(step.lensLabel)
                is Step.ReplaceOrAdd -> box[step.lensLabel] = step.lensFocalLength
            }
        }
        return boxes.withIndex().sumOf { (boxIndex, box) ->
            box.values.withIndex().sumOf { (lensIndex, lensFocalLength) ->
                (boxIndex + 1) * (lensIndex + 1) * lensFocalLength
            }
        }
    }
}
