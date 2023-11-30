package aoc2016

import AoCDay

// https://adventofcode.com/2016/day/9
object Day09 : AoCDay<Long>(
    title = "Explosives in Cyberspace",
    part1Answer = 74532,
    part2Answer = 11558231665,
) {
    private fun calculateDecompressedLength(input: String, versionTwo: Boolean): Long {
        fun calc(start: Int, end: Int): Long {
            var len = 0L
            var pos = start
            while (pos < end) {
                var c = input[pos++]
                if (c == '(') {
                    fun num(): Int {
                        var num = 0
                        c = input[pos++]
                        while (c in '0'..'9') {
                            num = num * 10 + (c - '0')
                            c = input[pos++]
                        }
                        return num
                    }

                    val chars = num()
                    require(c == 'x') { "Expected 'x' at position $pos" }
                    val times = num()
                    require(c == ')') { "Expected ')' at position $pos" }
                    len += times * if (versionTwo) calc(start = pos, end = pos + chars) else chars.toLong()
                    pos += chars
                } else {
                    len += 1
                }
            }
            return len
        }
        return calc(start = 0, end = input.length)
    }

    override fun part1(input: String) = calculateDecompressedLength(input, versionTwo = false)

    override fun part2(input: String) = calculateDecompressedLength(input, versionTwo = true)
}
