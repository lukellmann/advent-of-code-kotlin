package aoc2015

import AoCDay
import aoc2015.Day06.Instruction.*
import util.illegalInput
import util.match

// https://adventofcode.com/2015/day/6
object Day06 : AoCDay<Int>(
    title = "Probably a Fire Hazard",
    part1ExampleAnswer = 1_000_000 - 1_000 - 4,
    part1Answer = 400410,
    part2ExampleAnswer = 1_000_000 + 2_000 - 4,
    part2Answer = 15343601,
) {
    private enum class Instruction { TURN_ON, TOGGLE, TURN_OFF }

    private fun String.toInstruction() = when (this) {
        "turn on" -> TURN_ON
        "toggle" -> TOGGLE
        "turn off" -> TURN_OFF
        else -> illegalInput(this)
    }

    private val INSTRUCTION_REGEX = Regex("""(turn on|toggle|turn off) (\d+),(\d+) through (\d+),(\d+)""")

    private inline fun <T> configureLights(
        input: String,
        lights: Array<T>,
        configureSingleLight: (Instruction, lightRow: T, x: Int) -> Unit,
    ) {
        input
            .lineSequence()
            .map { line -> INSTRUCTION_REGEX.match(line) }
            .forEach { (instr, x1, y1, x2, y2) ->
                val instruction = instr.toInstruction()
                val xs = x1.toInt()..x2.toInt()
                val ys = y1.toInt()..y2.toInt()
                for (y in ys) {
                    for (x in xs) {
                        val lightRow = lights[y]
                        configureSingleLight(instruction, lightRow, x)
                    }
                }
            }
    }

    override fun part1(input: String): Int {
        val lights = Array(size = 1000) { BooleanArray(size = 1000) }

        configureLights(input, lights) { instruction, lightRow, x ->
            when (instruction) {
                TURN_ON -> lightRow[x] = true
                TOGGLE -> lightRow[x] = !lightRow[x]
                TURN_OFF -> lightRow[x] = false
            }
        }

        return lights.sumOf { lightRow -> lightRow.count { it } }
    }

    override fun part2(input: String): Int {
        val lights = Array(size = 1000) { IntArray(size = 1000) }

        configureLights(input, lights) { instruction, lightRow, x ->
            when (instruction) {
                TURN_ON -> lightRow[x] += 1
                TOGGLE -> lightRow[x] += 2
                TURN_OFF -> {
                    val prev = lightRow[x]
                    if (prev > 0) lightRow[x] = prev - 1
                }
            }
        }

        return lights.sumOf { lightRow -> lightRow.sum() }
    }
}
