package aoc2015

import AoCDay
import aoc2015.Day23.Instr.*
import util.illegalInput

// https://adventofcode.com/2015/day/23
object Day23 : AoCDay<UInt>(
    title = "Opening the Turing Lock",
    part1Answer = 307u,
    part2Answer = 160u,
) {
    private sealed interface Instr {
        class Hlf(val reg: Int) : Instr
        class Tpl(val reg: Int) : Instr
        class Inc(val reg: Int) : Instr
        class Jmp(val off: Int) : Instr
        class Jie(val reg: Int, val off: Int) : Instr
        class Jio(val reg: Int, val off: Int) : Instr
    }

    private fun parseRegister(input: String) = when (input) {
        "a" -> 0
        "b" -> 1
        else -> illegalInput(input)
    }

    private inline fun parseConditionalJump(args: String, ctor: (reg: Int, off: Int) -> Instr): Instr {
        val (reg, off) = args.split(", ", limit = 2)
        return ctor(parseRegister(reg), off.toInt())
    }

    private fun parseInstructions(input: String) = input.lines().map { line ->
        val (instr, args) = line.split(' ', limit = 2)
        when (instr) {
            "hlf" -> Hlf(reg = parseRegister(args))
            "tpl" -> Tpl(reg = parseRegister(args))
            "inc" -> Inc(reg = parseRegister(args))
            "jmp" -> Jmp(off = args.toInt())
            "jie" -> parseConditionalJump(args, ::Jie)
            "jio" -> parseConditionalJump(args, ::Jio)
            else -> illegalInput(instr)
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun runProgramAndGetB(instructions: List<Instr>, startValueA: UInt): UInt {
        var pc = 0
        val regs = UIntArray(size = 2)
        regs[0] = startValueA
        while (pc in instructions.indices) {
            when (val instr = instructions[pc]) {
                is Hlf -> regs[instr.reg] /= 2u
                is Tpl -> regs[instr.reg] *= 3u
                is Inc -> regs[instr.reg] += 1u
                is Jmp -> {
                    pc += instr.off
                    continue
                }
                is Jie -> if (regs[instr.reg] % 2u == 0u) {
                    pc += instr.off
                    continue
                }
                is Jio -> if (regs[instr.reg] == 1u) {
                    pc += instr.off
                    continue
                }
            }
            pc++
        }
        return regs[1]
    }

    override fun part1(input: String) = runProgramAndGetB(instructions = parseInstructions(input), startValueA = 0u)

    override fun part2(input: String) = runProgramAndGetB(instructions = parseInstructions(input), startValueA = 1u)
}
