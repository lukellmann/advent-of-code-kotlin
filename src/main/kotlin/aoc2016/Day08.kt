package aoc2016

import AoCDay
import aoc2016.Day08.Instruction.*
import util.illegalInput

// https://adventofcode.com/2016/day/8
object Day08 : AoCDay<Any>(
    title = "Two-Factor Authentication",
    part1ExampleAnswer = 6,
    part1Answer = 115,
    part2ExampleAnswer = """
        .#..#.#...........................................
        #.#...............................................
        ..................................................
        ..................................................
        ..................................................
        .#................................................
    """.trimIndent(),
    part2Answer = """
        ####.####.####.#...##..#.####.###..####..###...##.
        #....#....#....#...##.#..#....#..#.#......#.....#.
        ###..###..###...#.#.##...###..#..#.###....#.....#.
        #....#....#......#..#.#..#....###..#......#.....#.
        #....#....#......#..#.#..#....#.#..#......#..#..#.
        ####.#....####...#..#..#.#....#..#.#.....###..##..
    """.trimIndent(),
) {
    private sealed interface Instruction {
        class Rect(val width: Int, val height: Int) : Instruction
        class RotateRow(val y: Int, val shift: Int) : Instruction
        class RotateColumn(val x: Int, val shift: Int) : Instruction
    }

    private fun parseInstruction(input: String) = when {
        input.startsWith("rect ") -> {
            val (width, height) = input.drop(5).split('x', limit = 2).map(String::toInt)
            Rect(width, height)
        }
        input.startsWith("rotate row y=") -> {
            val (y, shift) = input.drop(13).split(" by ", limit = 2).map(String::toInt)
            RotateRow(y, shift)
        }
        input.startsWith("rotate column x=") -> {
            val (x, shift) = input.drop(16).split(" by ", limit = 2).map(String::toInt)
            RotateColumn(x, shift)
        }
        else -> illegalInput(input)
    }

    private fun runInstructionsAndGetScreen(instructions: String): List<BooleanArray> {
        val screen = List(6) { BooleanArray(50) }
        for (instruction in instructions.lineSequence().map(::parseInstruction)) {
            when (instruction) {
                is Rect -> for (rowIdx in 0..<instruction.height) {
                    val row = screen[rowIdx]
                    for (colIdx in 0..<instruction.width) {
                        row[colIdx] = true
                    }
                }
                is RotateRow -> {
                    val row = screen[instruction.y]
                    val replacementRow = BooleanArray(50)
                    for ((colIdx, pxl) in row.withIndex()) {
                        if (pxl) {
                            replacementRow[(colIdx + instruction.shift).mod(50)] = true
                        }
                    }
                    replacementRow.copyInto(row)
                }
                is RotateColumn -> {
                    val replacementColumn = BooleanArray(6)
                    for ((rowIdx, row) in screen.withIndex()) {
                        if (row[instruction.x]) {
                            replacementColumn[(rowIdx + instruction.shift).mod(6)] = true
                        }
                    }
                    for ((rowIdx, row) in screen.withIndex()) {
                        row[instruction.x] = replacementColumn[rowIdx]
                    }
                }
            }
        }
        return screen
    }

    override fun part1(input: String) = runInstructionsAndGetScreen(instructions = input)
        .sumOf { row -> row.count { pxl -> pxl } }

    override fun part2(input: String) = runInstructionsAndGetScreen(instructions = input)
        .joinToString(separator = "\n") { row ->
            row.joinToString(separator = "") { pxl -> if (pxl) "#" else "." }
        }
}
