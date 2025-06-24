package aoc2022

import AoCDay
import aoc2022.Day21.Job.YellNumber
import aoc2022.Day21.Job.YellOperation
import util.illegalInput

// https://adventofcode.com/2022/day/21
object Day21 : AoCDay<Long>(
    title = "Monkey Math",
    part1ExampleAnswer = 152,
    part1Answer = 82225382988628,
    part2ExampleAnswer = 301,
    part2Answer = 3429411069028,
) {
    private typealias Monkey = String

    private sealed interface Job {
        class YellNumber(val number: Long) : Job
        sealed class YellOperation(val left: Monkey, val right: Monkey) : Job {
            operator fun component1() = left
            operator fun component2() = right

            class Add(left: Monkey, right: Monkey) : YellOperation(left, right)
            class Subtract(left: Monkey, right: Monkey) : YellOperation(left, right)
            class Multiply(left: Monkey, right: Monkey) : YellOperation(left, right)
            class Divide(left: Monkey, right: Monkey) : YellOperation(left, right)
        }
    }

    private typealias MonkeyJobs = Map<Monkey, Job>

    private fun parseJob(job: String) = job.toLongOrNull()?.let(::YellNumber) ?: run {
        val (left, op, right) = job.split(' ', limit = 3)
        when (op) {
            "+" -> YellOperation.Add(left, right)
            "-" -> YellOperation.Subtract(left, right)
            "*" -> YellOperation.Multiply(left, right)
            "/" -> YellOperation.Divide(left, right)
            else -> illegalInput(job)
        }
    }

    private fun parseMonkeyJobs(input: String) = input
        .lineSequence()
        .map { line -> line.split(": ", limit = 2) }
        .associate { (monkey, job) -> monkey to parseJob(job) }

    private const val ROOT = "root"
    private const val HUMN = "humn"

    context(jobs: MonkeyJobs)
    private val Monkey.job
        get() = jobs.getValue(this@job)

    context(_: MonkeyJobs)
    private fun Monkey.yell(): Long = when (val job = job) {
        is YellNumber -> job.number
        is YellOperation.Add -> job.left.yell() + job.right.yell()
        is YellOperation.Subtract -> job.left.yell() - job.right.yell()
        is YellOperation.Multiply -> job.left.yell() * job.right.yell()
        is YellOperation.Divide -> job.left.yell() / job.right.yell()
    }

    context(_: MonkeyJobs)
    private operator fun Monkey.contains(other: Monkey): Boolean =
        this == other || when (val job = this.job) {
            is YellNumber -> false
            is YellOperation -> other in job.left || other in job.right
        }

    override fun part1(input: String) = context(parseMonkeyJobs(input)) { ROOT.yell() }

    override fun part2(input: String): Long {
        val jobs = parseMonkeyJobs(input)
        context(jobs - ROOT - HUMN) {
            var (monkey, target) = run {
                val (left, right) = jobs[ROOT] as YellOperation
                if (context(jobs) { HUMN in left }) left to right.yell() else right to left.yell()
            }
            while (monkey != HUMN) {
                val job = monkey.job as YellOperation
                val (left, right) = job
                val humnInLeft = HUMN in left
                val known = (if (humnInLeft) right else left).yell()
                target = when (job) {
                    is YellOperation.Add -> target - known
                    is YellOperation.Subtract -> if (humnInLeft) target + known else known - target
                    is YellOperation.Multiply -> target / known
                    is YellOperation.Divide -> if (humnInLeft) target * known else known / target
                }
                monkey = if (humnInLeft) left else right
            }
            return target
        }
    }
}
