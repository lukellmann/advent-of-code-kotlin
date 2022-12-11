package aoc2022

import AoCDay
import util.illegalInput

private typealias Operation = (Long) -> Long

// https://adventofcode.com/2022/day/11
object Day11 : AoCDay<Long>(
    title = "Monkey in the Middle",
    part1ExampleAnswer = 10605,
    part1Answer = 99852,
    part2ExampleAnswer = 2713310158,
    part2Answer = 25935263541,
) {
    private val MONKEY_REGEX = """
        Monkey (\d+):
         {2}Starting items: (\d+(?:, \d+)*)
         {2}Operation: new = old ([+*]) (old|\d+)
         {2}Test: divisible by (\d+)
         {4}If true: throw to monkey (\d+)
         {4}If false: throw to monkey (\d+)
    """.trimIndent().toRegex()

    private class Item(var worryLevel: Long)
    private class Monkey(
        val items: ArrayDeque<Item>,
        val operation: Operation,
        val testDivisor: Long,
        val monkeyTestTrue: Int,
        val monkeyTestFalse: Int,
        var inspectedItems: Long,
    )

    private val OLD_PLUS_OLD: Operation = { old -> old + old }
    private val OLD_TIMES_OLD: Operation = { old -> old * old }

    private fun parseMonkeys(input: String) = MONKEY_REGEX
        .findAll(input)
        .mapIndexed { index, monkey ->
            val (monkeyIndex, worryLevels, operator, secondOperand, testDivisor, monkeyTestTrue, monkeyTestFalse) =
                monkey.destructured
            require(index == monkeyIndex.toInt())

            val secondOperandValue = when (secondOperand) {
                "old" -> null
                else -> secondOperand.toLong()
            }
            Monkey(
                items = worryLevels.split(", ").mapTo(ArrayDeque()) { worryLevel -> Item(worryLevel.toLong()) },
                operation = when (operator) {
                    "+" -> if (secondOperandValue == null) OLD_PLUS_OLD else ({ old -> old + secondOperandValue })
                    "*" -> if (secondOperandValue == null) OLD_TIMES_OLD else ({ old -> old * secondOperandValue })
                    else -> illegalInput(operator)
                },
                testDivisor.toLong(),
                monkeyTestTrue.toInt(),
                monkeyTestFalse.toInt(),
                inspectedItems = 0,
            )
        }
        .toList()

    private inline fun getLevelOfMonkeyBusiness(monkeys: List<Monkey>, rounds: Int, adjustWorryLevel: Operation): Long {
        repeat(rounds) {
            for (monkey in monkeys) with(monkey) {
                var item = items.removeFirstOrNull()
                while (item != null) {
                    inspectedItems++
                    item.worryLevel = item.worryLevel.let(operation).let(adjustWorryLevel)
                    val throwMonkey = if (item.worryLevel % testDivisor == 0L) monkeyTestTrue else monkeyTestFalse
                    monkeys[throwMonkey].items.addLast(item)
                    item = items.removeFirstOrNull()
                }
            }
        }
        val (first, second) = monkeys.sortedByDescending { it.inspectedItems }
        return first.inspectedItems * second.inspectedItems
    }

    override fun part1(input: String): Long {
        val monkeys = parseMonkeys(input)
        return getLevelOfMonkeyBusiness(monkeys, rounds = 20, adjustWorryLevel = { it / 3 })
    }

    override fun part2(input: String): Long {
        val monkeys = parseMonkeys(input)
        val commonMultiple = monkeys.map { it.testDivisor }.distinct().reduce(Long::times)
        return getLevelOfMonkeyBusiness(monkeys, rounds = 10000, adjustWorryLevel = { it % commonMultiple })
    }
}
