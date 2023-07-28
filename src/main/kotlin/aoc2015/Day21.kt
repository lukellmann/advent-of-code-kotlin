package aoc2015

import AoCDay
import util.match

// https://adventofcode.com/2015/day/21
object Day21 : AoCDay<Int>(
    title = "RPG Simulator 20XX",
    part1Answer = 91,
    part2Answer = 158,
) {
    private class Item(val cost: Int, val damage: Int, val armor: Int)

    private val WEAPONS = listOf(
        Item(8, 4, 0),
        Item(10, 5, 0),
        Item(25, 6, 0),
        Item(40, 7, 0),
        Item(74, 8, 0),
    )
    private val ARMOR = listOf(
        Item(13, 0, 1),
        Item(31, 0, 2),
        Item(53, 0, 3),
        Item(75, 0, 4),
        Item(102, 0, 5),
    )
    private val RINGS = listOf(
        Item(25, 1, 0),
        Item(50, 2, 0),
        Item(100, 3, 0),
        Item(20, 0, 1),
        Item(40, 0, 2),
        Item(80, 0, 3),
    )

    private val ITEM_PERMUTATIONS = sequence {
        val armorPermutations = sequence { // 0-1 armor
            yield(emptyList())
            for (a in ARMOR) yield(listOf(a))
        }
        val ringPermutations = sequence { // 0-2 rings
            yield(emptyList())
            for (r in RINGS) yield(listOf(r))
            for (r1 in RINGS) {
                for (r2 in RINGS) {
                    if (r1 !== r2) yield(listOf(r1, r2))
                }
            }
        }
        for (w in WEAPONS) { // exactly one weapon
            for (a in armorPermutations) {
                for (r in ringPermutations) {
                    yield(a + r + w)
                }
            }
        }
    }

    private val BOSS_REGEX = """
        Hit Points: (\d+)
        Damage: (\d+)
        Armor: (\d+)
    """.trimIndent().toRegex()

    private fun parseBoss(input: String): Character {
        val (hp, damage, armor) = BOSS_REGEX.match(input).toList().map(String::toInt)
        return Character(hp, damage, armor)
    }

    private data class Character(val hp: Int, val damage: Int, val armor: Int) {
        init {
            require(damage >= 0 && armor >= 0)
        }

        fun attack(defender: Character): Character {
            val dmg = (this.damage - defender.armor).coerceAtLeast(1)
            return defender.copy(hp = defender.hp - dmg)
        }
    }

    private fun wouldPlayerWin(boss: Character, items: List<Item>): Boolean {
        val (damage, armor) = items.fold(0 to 0) { (damage, armor), item ->
            (damage + item.damage) to (armor + item.armor)
        }
        var player = Character(hp = 100, damage, armor)
        var enemy = boss
        while (true) {
            enemy = player.attack(enemy)
            if (enemy.hp <= 0) return true
            player = enemy.attack(player)
            if (player.hp <= 0) return false
        }
    }

    override fun part1(input: String): Int {
        val boss = parseBoss(input)
        return ITEM_PERMUTATIONS
            .filter { items -> wouldPlayerWin(boss, items) }
            .minOf { items -> items.sumOf { it.cost } }
    }

    override fun part2(input: String): Int {
        val boss = parseBoss(input)
        return ITEM_PERMUTATIONS
            .filterNot { items -> wouldPlayerWin(boss, items) }
            .maxOf { items -> items.sumOf { it.cost } }
    }
}
