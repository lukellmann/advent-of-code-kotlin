package aoc2015

import AoCDay
import java.security.MessageDigest

// https://adventofcode.com/2015/day/4
object Day4 : AoCDay<Int>(
    title = "The Ideal Stocking Stuffer",
    part1ExampleAnswer = 1048970,
    part1Answer = 254575,
    part2ExampleAnswer = 5714438,
    part2Answer = 1038736,
) {
    private const val ZERO: Byte = 0
    private const val HALF_BYTE_MASK = 0b1111_0000

    private fun mineAdventCoins(secretKey: String, leadingZeros: Int): Int {
        val md5 = MessageDigest.getInstance("MD5")

        // leadingZeros in hexadecimal -> searching for 4 x leadingZeros zero bits
        val fullBytes = leadingZeros / 2 // one byte has 8 bits
        val halfByte = leadingZeros % 2 != 0 // another half byte (4 bits) might have to be checked

        mineLoop@ for (number in 1..Int.MAX_VALUE) {
            val text = "$secretKey$number".toByteArray()
            val hash = md5.digest(text)

            // check leading zeros
            for (index in 0 until fullBytes) if (hash[index] != ZERO) continue@mineLoop
            if (halfByte && (hash[fullBytes].toInt() and HALF_BYTE_MASK) != 0) continue@mineLoop

            return number // we have 4 x leadingZero zero bits -> found answer
        }

        error("Could not mine AdventCoins with secret key $secretKey and $leadingZeros leading zeros")
    }

    override fun part1(input: String) = mineAdventCoins(secretKey = input, leadingZeros = 5)

    override fun part2(input: String) = mineAdventCoins(secretKey = input, leadingZeros = 6)
}
