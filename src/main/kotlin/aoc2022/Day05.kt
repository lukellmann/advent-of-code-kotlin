package aoc2022

import AoCDay
import util.*

private typealias Crate = Char

// https://adventofcode.com/2022/day/5
object Day05 : AoCDay<String>(
    title = "Supply Stacks",
    part1ExampleAnswer = "CMZ",
    part1Answer = "QNHWJVJZW",
    part2ExampleAnswer = "MCD",
    part2Answer = "BPCZJLFJW",
) {
    // parsing crate stacks assumes single digit stack numbers and will otherwise break
    private const val STACK_WIDTH = 4
    private fun parseCrateStacks(input: String): Array<Stack<Crate>> {
        // the lines in the crate part of the input all have the same length, filled with trailing spaces when needed
        val lineLength = input.lineSequence().first().length
        val stackCount = (lineLength + 1) / STACK_WIDTH // last stack doesn't have a space at the end -> + 1
        val crateStacks = Array(size = stackCount) { Stack<Crate>() }

        input // drop line containing stack numbers, we know they start at 1 and that we have stackCount stacks
            .substringBeforeLast('\n')
            .lineSequence()
            .forEach { line -> // read stacks from top to bottom -> push crates from bottom
                for ((stackIndex, linePosition) in (1..lineLength step STACK_WIDTH).withIndex()) {
                    val crate = line[linePosition]
                    if (crate != ' ') crateStacks[stackIndex].pushFromBottom(crate)
                }
            }

        return crateStacks
    }

    // parsing steps assumes single digit stack numbers (but could easily be changed by adjusting the regex)
    private data class Step(val amount: Int, val origin: Int, val target: Int)

    private val STEP_REGEX = Regex("""move (\d+) from (\d) to (\d)""")
    private fun parseSteps(input: String) = input
        .lineSequence()
        .map { line -> STEP_REGEX.match(line).toList().map(String::toInt) }
        .map { (amount, from, to) -> Step(amount, origin = from - 1, target = to - 1) } // 0-based indexing

    private fun rearrangeCrateStacksAndGetTopCrates(
        input: String,
        step: (amount: Int, origin: Stack<Crate>, target: Stack<Crate>) -> Unit,
    ): String {
        val (crateStacksInput, stepsInput) = input.split("\n\n", limit = 2)
        val crateStacks = parseCrateStacks(crateStacksInput)

        for ((amount, origin, target) in parseSteps(stepsInput)) {
            step(amount, crateStacks[origin], crateStacks[target])
        }

        return crateStacks.map { it.lastOrNull() ?: ' ' }.joinToString(separator = "")
    }

    override fun part1(input: String) = rearrangeCrateStacksAndGetTopCrates(
        input,
        step = { amount, origin, target ->
            repeat(amount) { target.push(origin.pop()) }
        },
    )

    override fun part2(input: String) = rearrangeCrateStacksAndGetTopCrates(
        input,
        step = { amount, origin, target ->
            List(size = amount) { origin.pop() }.asReversed().forEach(target::push)
        },
    )
}
