package aoc2016

import AoCDay
import util.illegalInput

// https://adventofcode.com/2016/day/4
object Day04 : AoCDay<Int>(
    title = "Security Through Obscurity",
    part1ExampleAnswer = 1514,
    part1Answer = 409147,
    part2Answer = 991,
) {
    private class Room(val encryptedName: String, val sectorId: Int, val checksum: String)

    private fun parseRoom(input: String): Room {
        val encryptedName = input.substringBeforeLast('-')
        val (sectorId, checksum) = input.substringAfterLast('-').split('[', limit = 2)
        return Room(encryptedName, sectorId.toInt(), checksum.substringBeforeLast(']'))
    }

    private val Room.isReal: Boolean
        get() {
            if (checksum.length != 5) return false
            val counts = encryptedName.filter { it != '-' }.groupingBy { it }.eachCount()
            var letter = checksum[0]
            var count = counts[letter] ?: return false
            if (count != counts.values.maxOrNull()) return false
            for (i in 1..4) {
                val l = checksum[i]
                val c = counts[l] ?: return false
                if (c > count || c == count && l <= letter) return false
                letter = l
                count = c
            }
            return true
        }

    private const val LETTER_COUNT = 'z' - 'a' + 1
    private val Room.name
        get() = encryptedName.map { letter ->
            when (letter) {
                in 'a'..'z' -> Char((letter - 'a' + sectorId) % LETTER_COUNT + 'a'.code)
                '-' -> ' '
                else -> illegalInput(letter)
            }
        }.joinToString(separator = "")

    override fun part1(input: String) = input
        .lineSequence()
        .map(::parseRoom)
        .filter { it.isReal }
        .sumOf { it.sectorId }

    override fun part2(input: String) = input
        .lineSequence()
        .map(::parseRoom)
        .single { it.name == "northpole object storage" }
        .sectorId
}
