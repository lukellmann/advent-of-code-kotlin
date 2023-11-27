package aoc2016

import AoCDay
import util.illegalInput

// https://adventofcode.com/2016/day/2
object Day02 : AoCDay<String>(
    title = "Bathroom Security",
    part1ExampleAnswer = "1985",
    part1Answer = "76792",
    part2ExampleAnswer = "5DB3",
    part2Answer = "A7AC3",
) {
    private fun getBathroomCode(instructions: String, keypad: List<List<Char>>): String {
        var code = ""
        var row = keypad.indexOfFirst { '5' in it }
        var col = keypad[row].indexOf('5')
        for (line in instructions.lines()) {
            for (instruction in line) { // "search button"
                when (instruction) {
                    'U' -> if (row > 0 && keypad[row - 1][col] != ' ') row -= 1
                    'D' -> if (row < keypad.lastIndex && keypad[row + 1][col] != ' ') row += 1
                    'L' -> if (col > 0 && keypad[row][col - 1] != ' ') col -= 1
                    'R' -> if (col < keypad[row].lastIndex && keypad[row][col + 1] != ' ') col += 1
                    else -> illegalInput(instruction)
                }
            }
            code += keypad[row][col] // "press button"
        }
        return code
    }

    override fun part1(input: String) = getBathroomCode(
        instructions = input,
        keypad = listOf(
            listOf('1', '2', '3'),
            listOf('4', '5', '6'),
            listOf('7', '8', '9'),
        ),
    )

    override fun part2(input: String) = getBathroomCode(
        instructions = input,
        keypad = listOf(
            listOf(' ', ' ', '1', ' ', ' '),
            listOf(' ', '2', '3', '4', ' '),
            listOf('5', '6', '7', '8', '9'),
            listOf(' ', 'A', 'B', 'C', ' '),
            listOf(' ', ' ', 'D', ' ', ' '),
        ),
    )
}
