package aoc2015

import AoCDay
import util.match

// https://adventofcode.com/2015/day/22
object Day22 : AoCDay<Int>(
    title = "Wizard Simulator 20XX",
    part1Answer = 1824,
    part2Answer = 1937,
) {
    private class Spell(val mana: Int, val effect: Effect? = null, val damage: Int = 0, val heal: Int = 0)
    private class Effect(val duration: Int, val armor: Int = 0, val damage: Int = 0, val mana: Int = 0)

    private val SPELLS = listOf(
        Spell(mana = 53, damage = 4),
        Spell(mana = 73, damage = 2, heal = 2),
        Spell(mana = 113, Effect(duration = 6, armor = 7)),
        Spell(mana = 173, Effect(duration = 6, damage = 3)),
        Spell(mana = 229, Effect(duration = 5, mana = 101)),
    )

    private val BOSS_REGEX = """
        Hit Points: (\d+)
        Damage: (\d+)
    """.trimIndent().toRegex()

    private fun parseBoss(input: String): Boss {
        val (hp, damage) = BOSS_REGEX.match(input).toList().map(String::toInt)
        return Boss(hp, damage)
    }

    private data class Boss(val hp: Int, val damage: Int) {
        init {
            require(damage >= 0)
        }
    }

    private data class Player(
        val hp: Int,
        val armor: Int,
        val mana: Int,
        val manaSpent: Int,
        val effects: Map<Effect, Int>,
    )

    private sealed interface State {
        object Lost : State
        class Won(val manaSpent: Int) : State
        class Undecided(val player: Player, val boss: Boss, private val hard: Boolean) : State {
            fun copy(player: Player = this.player, boss: Boss = this.boss) = when {
                player.hp <= 0 -> Lost
                boss.hp <= 0 -> Won(manaSpent = player.manaSpent)
                else -> Undecided(player, boss, hard)
            }

            fun lose1HpIfHard() = if (hard) copy(player = player.copy(hp = player.hp - 1)) else this
        }

        fun applyEffects(): State {
            if (this !is Undecided) return this
            val effects = player.effects.keys
            val armor = effects.sumOf { it.armor }
            val damage = effects.sumOf { it.damage }
            val mana = effects.sumOf { it.mana }
            return copy(
                player = player.copy(
                    armor = armor,
                    mana = player.mana + mana,
                    effects = player.effects.filterValues { it > 1 }.mapValues { (_, d) -> d - 1 },
                ),
                boss = boss.copy(hp = boss.hp - damage),
            )
        }

        fun castSpell(spell: Spell) = if (this !is Undecided) this else copy(
            player = player.copy(
                hp = player.hp + spell.heal,
                mana = player.mana - spell.mana,
                manaSpent = player.manaSpent + spell.mana,
                effects = spell.effect?.let { e -> player.effects + (e to e.duration) } ?: player.effects,
            ),
            boss = boss.copy(hp = boss.hp - spell.damage),
        )

        fun bossAttack(): State {
            if (this !is Undecided) return this
            val dmg = (boss.damage - player.armor).coerceAtLeast(1)
            return copy(player = player.copy(hp = player.hp - dmg))
        }
    }

    private inline fun State.ifUndecided(action: State.Undecided.() -> Int?) = when (this) {
        State.Lost -> null
        is State.Won -> manaSpent
        is State.Undecided -> action()
    }

    private fun bossTurn(state: State.Undecided): Int? = state
        .applyEffects()
        .bossAttack()
        .ifUndecided(::playerTurn)

    private fun playerTurn(state: State.Undecided): Int? = state
        .lose1HpIfHard()
        .applyEffects()
        .ifUndecided {
            SPELLS.filter { it.mana <= player.mana && it.effect !in player.effects }
                .minOfWithOrNull(nullsLast()) { castSpell(it).ifUndecided(::bossTurn) }
        }

    private val INITIAL_PLAYER = Player(hp = 50, armor = 0, mana = 500, manaSpent = 0, effects = emptyMap())
    private fun leastManaSpentToWin(input: String, hard: Boolean) =
        playerTurn(State.Undecided(player = INITIAL_PLAYER, boss = parseBoss(input), hard)) ?: error("can't win")

    override fun part1(input: String) = leastManaSpentToWin(input, hard = false)

    override fun part2(input: String) = leastManaSpentToWin(input, hard = true)
}
