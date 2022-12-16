package aoc2015

import AoCDay
import kotlin.math.sqrt

// https://adventofcode.com/2015/day/20
object Day20 : AoCDay<Int>(
    title = "Infinite Elves and Infinite Houses",
    part1Answer = 665280,
    part2Answer = 705600,
) {
    private fun divisors(n: Int) = buildSet {
        for (divisor in 1..sqrt(n.toDouble()).toInt() + 1) {
            if (n % divisor == 0) {
                add(divisor)
                add(n / divisor)
            }
        }
    }

    private inline fun lowestHouseNumber(minPresents: Int, presents: (house: Int, elves: Set<Int>) -> Int) =
        (1..Int.MAX_VALUE).first { house ->
            val elves = divisors(house)
            presents(house, elves) >= minPresents
        }

    override fun part1(input: String) = lowestHouseNumber(
        minPresents = input.toInt(),
        presents = { _, elves -> elves.sum() * 10 },
    )

    override fun part2(input: String) = lowestHouseNumber(
        minPresents = input.toInt(),
        presents = { house, elves -> elves.sumOf { elf -> if (house / elf <= 50) elf else 0 } * 11 },
    )
}
