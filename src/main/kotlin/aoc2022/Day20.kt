package aoc2022

import AoCDay
import kotlin.math.abs

// https://adventofcode.com/2022/day/20
object Day20 : AoCDay<Long>(
    title = "Grove Positioning System",
    part1ExampleAnswer = 3,
    part1Answer = 19559,
    part2ExampleAnswer = 1623178306,
    part2Answer = 912226207972,
) {
    private fun parseNumbers(file: String) = file.lines().map(String::toLong)

    private class NumbersList(numbers: List<Long>) {
        private class Node(val number: Long) {
            lateinit var left: Node
            lateinit var right: Node
        }

        // all nodes in original order
        private val nodes = numbers.map(::Node)
        private val size get() = nodes.size

        init {
            for ((i, node) in nodes.withIndex()) {
                node.left = nodes[(i - 1).mod(size)]
                node.right = nodes[(i + 1).mod(size)]
            }
        }

        fun mix() {
            for (node in nodes) {
                // node does not count when moving -> % (size - 1)
                val move = (node.number % (size - 1)).toInt()
                if (move == 0) continue
                // remove node from list
                node.left.right = node.right
                node.right.left = node.left
                // insert node again at correct position
                var target = node
                if (move < 0) {
                    repeat(abs(move)) {
                        target = target.left
                    }
                    node.right = target
                    target.left.right = node
                    node.left = target.left
                    target.left = node
                } else {
                    repeat(move) {
                        target = target.right
                    }
                    node.left = target
                    target.right.left = node
                    node.right = target.right
                    target.right = node
                }
            }
        }

        fun sumOfGroveCoordinates(): Long {
            val groveCoordinateIndices = listOf(1000, 2000, 3000).map { it % size }
            var sum = 0L
            var node = nodes.first { it.number == 0L }
            for (i in 1..groveCoordinateIndices.max()) {
                node = node.right
                // in case, more than one index point to the same node
                repeat(groveCoordinateIndices.count { it == i }) {
                    sum += node.number
                }
            }
            return sum
        }
    }

    override fun part1(input: String): Long {
        val list = NumbersList(parseNumbers(input))
        list.mix()
        return list.sumOfGroveCoordinates()
    }

    override fun part2(input: String): Long {
        val list = NumbersList(parseNumbers(input).map { it * 811589153 })
        repeat(10) {
            list.mix()
        }
        return list.sumOfGroveCoordinates()
    }
}
