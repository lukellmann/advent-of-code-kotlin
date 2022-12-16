package aoc2022

import AoCDay
import util.match
import kotlin.math.abs
import kotlin.math.max

// https://adventofcode.com/2022/day/15
object Day15 : AoCDay<Long>(
    title = "Beacon Exclusion Zone",
    part1Answer = 5832528,
    part2Answer = 13360899249595,
) {
    private fun manhattanDistance(x1: Int, y1: Int, x2: Int, y2: Int) = abs(x1 - x2) + abs(y1 - y2)
    private fun manhattanDistance(p: Position, x: Int, y: Int) = manhattanDistance(p.x, p.y, x, y)
    private fun manhattanDistance(p1: Position, p2: Position) = manhattanDistance(p1.x, p1.y, p2.x, p2.y)

    private data class Position(val x: Int, val y: Int)
    private data class SensorReport(val sensor: Position, val beacon: Position, val radius: Int)

    private val SENSOR_REPORT_REGEX =
        Regex("""Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""")

    private fun parseSensorReports(input: String) = input
        .lines()
        .map { line -> SENSOR_REPORT_REGEX.match(line) }
        .map { (xs, ys, xb, yb) ->
            val sensor = Position(xs.toInt(), ys.toInt())
            val beacon = Position(xb.toInt(), yb.toInt())
            SensorReport(sensor, beacon, radius = manhattanDistance(sensor, beacon))
        }

    private val IntRange.size get() = if (isEmpty()) 0 else last - first + 1

    override fun part1(input: String): Long {
        val row = 2000000

        data class XRangesAndBeacons(val xRanges: List<IntRange>, val beacons: List<Position>)

        val (xRanges, beacons) = parseSensorReports(input)
            .fold(
                initial = XRangesAndBeacons(xRanges = emptyList(), beacons = emptyList())
            ) { (xRanges, beacons), (sensor, beacon, radius) ->
                val dy = abs(sensor.y - row)
                val dx = radius - dy
                val xrs = if (dy <= radius) xRanges.plusElement(sensor.x - dx..sensor.x + dx) else xRanges
                val bs = if (beacon.y == row) beacons + beacon else beacons
                XRangesAndBeacons(xRanges = xrs, beacons = bs)
            }

        val mergedXRanges = xRanges
            .sortedBy { it.first }
            .fold(initial = emptyList<IntRange>()) { mergedXRanges, xRange ->
                val prev = mergedXRanges.lastOrNull()
                when {
                    prev == null -> listOf(xRange)
                    xRange.first in prev -> {
                        val mergedXRange = prev.first..max(xRange.last, prev.last)
                        mergedXRanges.dropLast(1).plusElement(mergedXRange)
                    }
                    else -> mergedXRanges.plusElement(xRange)
                }
            }

        val positionsInRow = mergedXRanges.fold(initial = 0L) { acc, xRange -> acc + xRange.size }
        val beaconsInRow = mergedXRanges.count { xRange -> beacons.any { it.x in xRange } }
        return positionsInRow - beaconsInRow
    }

    // this is somewhat brute force, could be solved more efficiently using some clever geometry I couldn't come up with
    override fun part2(input: String): Long {
        val range = 0..4000000
        val sensorReports = parseSensorReports(input)

        for ((sensor, _, radius) in sensorReports) {
            // start at top of sensor diamond and walk clockwise
            var x = sensor.x
            var y = sensor.y - (radius + 1)
            repeat(4) { time ->
                val dx = if (time == 0 || time == 3) 1 else -1
                val dy = if (time == 0 || time == 1) 1 else -1
                repeat(radius + 1) {
                    if (
                        x in range && y in range &&
                        sensorReports.all { (sensor, _, radius) -> manhattanDistance(sensor, x, y) > radius }
                    ) {
                        return x.toLong() * 4000000 + y
                    }
                    x += dx
                    y += dy
                }
            }
        }

        error("Did not find the distress beacon")
    }
}
