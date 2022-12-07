package aoc2015

import AoCDay
import aoc2015.Day7.Signal.*
import util.illegalInput

private typealias WireId = String

// https://adventofcode.com/2015/day/7
object Day7 : AoCDay<UShort>(
    title = "Some Assembly Required",
    part1ExampleAnswer = 65412u,
    part1Answer = 16076u,
    part2Answer = 2797u,
) {
    private sealed interface Signal {
        class Value(val value: UShort) : Signal
        class Wire(val wireId: WireId) : Signal
        class And(val left: Signal, val right: Signal) : Signal
        class Or(val left: Signal, val right: Signal) : Signal
        class LShift(val left: Signal, val right: Signal) : Signal
        class RShift(val left: Signal, val right: Signal) : Signal
        class Not(val signal: Signal) : Signal
    }

    private const val AND = " AND "
    private const val OR = " OR "
    private const val LSHIFT = " LSHIFT "
    private const val RSHIFT = " RSHIFT "
    private const val NOT = "NOT "

    private inline fun parseBinarySignal(signal: String, op: String, ctor: (Signal, Signal) -> Signal): Signal {
        val (left, right) = signal.split(op).map(::parseSignal)
        return ctor(left, right)
    }

    private fun parseSignal(signal: String): Signal = when {
        signal.all { it in '0'..'9' } -> Value(value = signal.toUShort())
        signal.all { it in 'a'..'z' } -> Wire(wireId = signal)
        AND in signal -> parseBinarySignal(signal, AND, ::And)
        OR in signal -> parseBinarySignal(signal, OR, ::Or)
        LSHIFT in signal -> parseBinarySignal(signal, LSHIFT, ::LShift)
        RSHIFT in signal -> parseBinarySignal(signal, RSHIFT, ::RShift)
        NOT in signal -> Not(signal = parseSignal(signal.substringAfter(NOT)))
        else -> illegalInput(signal)
    }

    private fun parseWires(input: String) = input
        .lineSequence()
        .map { line -> line.split(" -> ") }
        .associate { (signal, wireId) -> wireId to parseSignal(signal) }

    private fun Signal.eval(wires: Map<WireId, Signal>, mem: MutableMap<WireId, UShort>): UShort = when (this) {
        is Value -> value
        is Wire -> mem[wireId] ?: wires.getValue(wireId).eval(wires, mem).also { mem[wireId] = it }
        is And -> left.eval(wires, mem) and right.eval(wires, mem)
        is Or -> left.eval(wires, mem) or right.eval(wires, mem)
        is LShift -> (left.eval(wires, mem).toUInt() shl right.eval(wires, mem).toInt()).toUShort()
        is RShift -> (left.eval(wires, mem).toUInt() shr right.eval(wires, mem).toInt()).toUShort()
        is Not -> signal.eval(wires, mem).inv()
    }

    private fun Map<WireId, Signal>.evaluateWire(wireId: WireId) =
        getValue(wireId).eval(wires = this, mem = hashMapOf())

    override fun part1(input: String) = parseWires(input).evaluateWire("a")

    override fun part2(input: String): UShort {
        val wires = parseWires(input)
        val a = wires.evaluateWire("a")
        val newWires = wires + ("b" to Value(a)) // overrides exising b
        return newWires.evaluateWire("a")
    }
}
