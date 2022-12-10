package aoc2015

import AoCDay
import util.match

// https://adventofcode.com/2015/day/14
object Day14 : AoCDay<Int>(
    title = "Reindeer Olympics",
    part1Answer = 2696,
    part2Answer = 1084,
) {
    private class Reindeer(val speed: Int, val goSeconds: Int, val restSeconds: Int) {
        fun travelFor(seconds: Int): Int {
            var distance = 0
            var secondsLeft = seconds
            while (true) {
                repeat(goSeconds) {
                    if (secondsLeft <= 0) return distance
                    secondsLeft--
                    distance += speed
                }
                secondsLeft -= restSeconds
            }
        }
    }

    private val REINDEER_REGEX =
        Regex("""\w+ can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds\.""")

    private fun parseReindeer(input: String) = input
        .lines()
        .map { line -> REINDEER_REGEX.match(line) }
        .map { (speed, goSeconds, restSeconds) -> Reindeer(speed.toInt(), goSeconds.toInt(), restSeconds.toInt()) }

    override fun part1(input: String) = parseReindeer(input).maxOf { reindeer -> reindeer.travelFor(seconds = 2503) }

    override fun part2(input: String): Int {
        class ReindeerState(
            val reindeer: Reindeer,
            var go: Boolean = true,
            var secondsLeft: Int,
            var distance: Int = 0,
            var points: Int = 0,
        )

        val reindeerStates = parseReindeer(input).map { ReindeerState(it, secondsLeft = it.goSeconds) }

        repeat(2503) {
            for (state in reindeerStates) with(state) {
                if (secondsLeft <= 0) {
                    go = !go
                    secondsLeft = if (go) reindeer.goSeconds else reindeer.restSeconds
                }
                secondsLeft--
                if (go) distance += reindeer.speed
            }
            val maxDistance = reindeerStates.maxOf { it.distance }
            reindeerStates.forEach { if (it.distance == maxDistance) it.points++ }
        }

        return reindeerStates.maxOf { it.points }
    }
}
