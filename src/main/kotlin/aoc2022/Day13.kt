package aoc2022

import AoCDay
import util.illegalInput

// https://adventofcode.com/2022/day/13
object Day13 : AoCDay<Int>(
    title = "Distress Signal",
    part1ExampleAnswer = 13,
    part1Answer = 6046,
    part2ExampleAnswer = 140,
    part2Answer = 21423,
) {
    private sealed class PacketData : Comparable<PacketData> {
        class Integer(val integer: Int) : PacketData()
        class List(val list: kotlin.collections.List<PacketData>) : PacketData() {
            constructor(element: PacketData) : this(listOf(element))
        }

        final override fun compareTo(other: PacketData): Int {
            return when (this) {
                is Integer -> when (other) {
                    is Integer -> this.integer.compareTo(other.integer)
                    is List -> List(this).compareTo(other)
                }
                is List -> when (other) {
                    is Integer -> this.compareTo(List(other))
                    is List -> {
                        val left = this.list.iterator()
                        val right = other.list.iterator()
                        while (left.hasNext() && right.hasNext()) {
                            val comparison = left.next().compareTo(right.next())
                            if (comparison != 0) return comparison
                        }
                        this.list.size.compareTo(other.list.size)
                    }
                }
            }
        }
    }

    private fun parsePacket(input: String): PacketData.List {
        fun parsePacketDataList(start: Int): IndexedValue<PacketData.List> {
            var index = start
            val list = PacketData.List(buildList {
                var integer = ""
                fun addInteger() {
                    if (integer.isNotEmpty()) {
                        add(PacketData.Integer(integer.toInt()))
                        integer = ""
                    }
                }
                while (true) {
                    when (val char = input[index++]) {
                        in '0'..'9' -> integer += char
                        ',' -> addInteger()
                        '[' -> {
                            val (skipIndex, list) = parsePacketDataList(start = index)
                            add(list)
                            index = skipIndex
                        }
                        ']' -> {
                            addInteger()
                            break
                        }
                        else -> illegalInput(input)
                    }
                }
            })
            return IndexedValue(index, list)
        }
        require(input.first() == '[')
        val (index, packet) = parsePacketDataList(start = 1)
        check(index == input.length) { "input wasn't fully consumed, stopped at $index for length ${input.length}" }
        return packet
    }

    private fun parsePacketPairs(input: String) = input
        .lineSequence()
        .chunked(3) // third is just a blank line or missing -> ignore
        .map { (first, second) -> Pair(parsePacket(first), parsePacket(second)) }

    private fun parsePackets(input: String) = input
        .lineSequence()
        .filter { line -> line.isNotBlank() }
        .map(::parsePacket)

    override fun part1(input: String) = parsePacketPairs(input)
        .withIndex()
        .filter { (_, packetPair) -> packetPair.first < packetPair.second }
        .sumOf { packetPair -> packetPair.index + 1 } // 1-based indexing

    override fun part2(input: String): Int {
        val dividerPacket1 = parsePacket("[[2]]")
        val dividerPacket2 = parsePacket("[[6]]")
        val (indexDivider1, indexDivider2) = (parsePackets(input) + dividerPacket1 + dividerPacket2)
            .sorted()
            .withIndex()
            .filter { (_, packet) -> packet === dividerPacket1 || packet === dividerPacket2 }
            .map { dividerPacket -> dividerPacket.index + 1 } // 1-based indexing
            .toList()
            .also { check(it.size == 2) { "Expected 2 divider packets but found ${it.size}" } }
        return indexDivider1 * indexDivider2
    }
}
