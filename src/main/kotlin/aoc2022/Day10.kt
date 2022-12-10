package aoc2022

import AoCDay
import util.illegalInput

// https://adventofcode.com/2022/day/10
object Day10 : AoCDay<Any>(
    title = "Cathode-Ray Tube",
    part1ExampleAnswer = 13140,
    part1Answer = 13740,
    part2ExampleAnswer = """
        ##..##..##..##..##..##..##..##..##..##..
        ###...###...###...###...###...###...###.
        ####....####....####....####....####....
        #####.....#####.....#####.....#####.....
        ######......######......######......####
        #######.......#######.......#######.....
    """.trimIndent(),
    part2Answer = """
        ####.#..#.###..###..####.####..##..#....
        ...#.#..#.#..#.#..#.#....#....#..#.#....
        ..#..#..#.#..#.#..#.###..###..#....#....
        .#...#..#.###..###..#....#....#....#....
        #....#..#.#....#.#..#....#....#..#.#....
        ####..##..#....#..#.#....####..##..####.
    """.trimIndent(),
) {
    private data class Cycle(val cycleNumber: Int, val xDuringCycle: Int)

    private fun runProgramAndGetCycles(program: String) = sequence {
        var x = 1
        var cycle = 1
        suspend fun SequenceScope<Cycle>.consumeCycle() = yield(Cycle(cycleNumber = cycle++, xDuringCycle = x))
        for (instruction in program.lineSequence()) {
            when {
                instruction == "noop" -> consumeCycle()
                instruction.startsWith("addx ") -> {
                    consumeCycle()
                    consumeCycle()
                    x += instruction.removePrefix("addx ").toInt()
                }
                else -> illegalInput(instruction)
            }
        }
    }

    private val CYCLE_NUMBERS_FOR_PART1 = setOf(20, 60, 100, 140, 180, 220)

    override fun part1(input: String) = runProgramAndGetCycles(program = input)
        .filter { it.cycleNumber in CYCLE_NUMBERS_FOR_PART1 }
        .sumOf { (cycleNumber, xDuringCycle) -> cycleNumber * xDuringCycle }

    override fun part2(input: String) = runProgramAndGetCycles(program = input)
        .map { (cycleNumber, xDuringCycle) ->
            val crtPosition = (cycleNumber - 1) % 40
            val sprite = xDuringCycle - 1..xDuringCycle + 1
            if (crtPosition in sprite) '#' else '.'
        }
        .chunked(40)
        .joinToString(separator = "\n") { crtRow -> crtRow.joinToString(separator = "") }
}
