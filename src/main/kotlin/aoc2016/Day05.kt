package aoc2016

import AoCDay
import java.security.MessageDigest

// https://adventofcode.com/2016/day/5
object Day05 : AoCDay<String>(
    title = "How About a Nice Game of Chess?",
    part1ExampleAnswer = "18f47a30",
    part1Answer = "4543c154",
    part2ExampleAnswer = "05ace8e3",
    part2Answer = "1050cbbd",
) {
    private const val ZERO: Byte = 0
    private val HEX = "0123456789abcdef".toCharArray()

    private inline fun onInterestingHash(doorId: String, block: (hex6: Char, hex7: Char) -> Unit): Nothing {
        /** similar to [aoc2015.Day04] */
        val md5 = MessageDigest.getInstance("MD5")
        for (index in 0..Int.MAX_VALUE) {
            val hash = md5.digest("$doorId$index".toByteArray())
            // looking for 5 leading zeros in hexadecimal -> 2.5 bytes have to be 0
            val byte3 = hash[2].toInt()
            if ((byte3 and 0xf0) == 0 && hash[0] == ZERO && hash[1] == ZERO) {
                block(HEX[byte3], HEX[hash[3].toUByte().toInt() shr 4])
            }
        }
        error("Index ran out of Int range")
    }

    override fun part1(input: String): String {
        var pw = ""
        onInterestingHash(doorId = input) { hex6, _ ->
            pw += hex6
            if (pw.length == 8) return pw
        }
    }

    override fun part2(input: String): String {
        val pw = CharArray(8)
        onInterestingHash(doorId = input) { hex6, hex7 ->
            val position = hex6 - '0'
            if (position in 0..7 && pw[position] == '\u0000') {
                pw[position] = hex7
                if (pw.all { it != '\u0000' }) return pw.concatToString()
            }
        }
    }
}
