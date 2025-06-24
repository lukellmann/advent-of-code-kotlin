package aoc2024

import AoCDay

// https://adventofcode.com/2024/day/3
object Day03 : AoCDay<Int>(
    title = "Mull It Over",
    part1ExampleAnswer = 161,
    part1Answer = 179834255,
    part2ExampleAnswer = 48,
    part2Answer = 80570939,
) {
    private sealed interface Instruction {
        data class Mul(val left: Int, val right: Int) : Instruction
        object Do : Instruction
        object Dont : Instruction
    }

    private enum class Character {
        UNKNOWN, M, U, L, LPAREN_MUL, LEFT1, LEFT2, LEFT3, COMMA, RIGHT1, RIGHT2, RIGHT3, D, O, LPAREN_DO, N, QUOTE, T,
        LPAREN_DONT,
    }

    private fun parseInstructions(input: String) = sequence {
        var previousChar = Character.UNKNOWN
        var left = 0
        var right = 0
        for (char in input) {
            previousChar = when (previousChar) {
                UNKNOWN if char == 'm' -> M
                M if char == 'u' -> U
                U if char == 'l' -> L
                L if char == '(' -> LPAREN_MUL
                LPAREN_MUL if char in '0'..'9' -> {
                    left = char - '0'
                    LEFT1
                }
                LEFT1, LEFT2 -> when (char) {
                    in '0'..'9' -> {
                        left = left * 10 + (char - '0')
                        if (previousChar == LEFT1) LEFT2 else LEFT3
                    }
                    ',' -> COMMA
                    else -> UNKNOWN
                }
                LEFT3 if char == ',' -> COMMA
                COMMA if char in '0'..'9' -> {
                    right = char - '0'
                    RIGHT1
                }
                RIGHT1, RIGHT2 -> when (char) {
                    in '0'..'9' -> {
                        right = right * 10 + (char - '0')
                        if (previousChar == RIGHT1) RIGHT2 else RIGHT3
                    }
                    ')' -> {
                        yield(Instruction.Mul(left, right))
                        UNKNOWN
                    }
                    else -> UNKNOWN
                }
                RIGHT3 -> {
                    if (char == ')') yield(Instruction.Mul(left, right))
                    UNKNOWN
                }

                UNKNOWN if char == 'd' -> D
                D if char == 'o' -> O

                O if char == '(' -> LPAREN_DO
                LPAREN_DO -> {
                    if (char == ')') yield(Instruction.Do)
                    UNKNOWN
                }

                O if char == 'n' -> N
                N if char == '\'' -> QUOTE
                QUOTE if char == 't' -> T
                T if char == '(' -> LPAREN_DONT
                LPAREN_DONT -> {
                    if (char == ')') yield(Instruction.Dont)
                    UNKNOWN
                }

                UNKNOWN, M, U, L, LPAREN_MUL, LEFT3, COMMA, D, O, N, QUOTE, T -> UNKNOWN
            }
        }
    }

    override fun part1(input: String) =
        parseInstructions(input).filterIsInstance<Instruction.Mul>().sumOf { (left, right) -> left * right }

    override fun part2(input: String): Int {
        data class Acc(val sum: Int, val mulEnabled: Boolean)
        return parseInstructions(input)
            .fold(initial = Acc(sum = 0, mulEnabled = true)) { acc, instruction ->
                when (instruction) {
                    Instruction.Do -> acc.copy(mulEnabled = true)
                    Instruction.Dont -> acc.copy(mulEnabled = false)
                    is Instruction.Mul if !acc.mulEnabled -> acc
                    is Instruction.Mul -> {
                        val (left, right) = instruction
                        acc.copy(sum = acc.sum + left * right)
                    }
                }
            }
            .sum
    }
}
