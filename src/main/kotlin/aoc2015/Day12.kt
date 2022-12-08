package aoc2015

import AoCDay
import util.Stack
import util.peek
import util.pop
import util.push

// https://adventofcode.com/2015/day/12
object Day12 : AoCDay<Int>(
    title = "JSAbacusFramework.io",
    part1ExampleAnswer = 6,
    part1Answer = 191164,
    part2ExampleAnswer = 4,
    part2Answer = 87842,
) {
    override fun part1(input: String): Int {
        val num = StringBuilder()
        var sum = 0
        for (char in input) {
            if (char == '-' || char in '0'..'9') {
                num.append(char)
            } else if (num.isNotEmpty()) {
                sum += num.toString().toInt()
                num.clear()
            }
        }
        return sum
    }

    override fun part2(input: String): Int {
        val num = StringBuilder()
        val sums = Stack<Int>().apply { push(0) }
        val ignore = Stack<Boolean>().apply { push(false) }
        for ((index, char) in input.withIndex()) {
            if (char == '-' || char in '0'..'9') {
                num.append(char)
            } else if (num.isNotEmpty()) {
                if (!ignore.peek()) sums.push(sums.pop() + num.toString().toInt())
                num.clear()
            }
            when (char) {
                '{' -> {
                    sums.push(0)
                    ignore.push(ignore.peek())
                }
                '}' -> {
                    sums.push(sums.pop() + sums.pop())
                    ignore.pop()
                }
                ':' -> if (input.substring(index + 1).startsWith("\"red\"")) {
                    sums.pop(); sums.push(0)
                    ignore.pop(); ignore.push(true)
                }
            }
        }
        return sums.peek()
    }
}
