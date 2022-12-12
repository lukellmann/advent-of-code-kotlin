@file:Suppress("DuplicatedCode")

package aoc2015

import AoCDay
import util.illegalInput

// https://adventofcode.com/2015/day/15
object Day15 : AoCDay<Int>(
    title = "Science for Hungry People",
    part1ExampleAnswer = 62842880,
    part1Answer = 18965440,
    part2ExampleAnswer = 57600000,
    part2Answer = 15862900,
) {
    private data class Ingredient(
        val capacity: Int,
        val durability: Int,
        val flavor: Int,
        val texture: Int,
        val calories: Int,
    )

    private val INGREDIENT_REGEX =
        Regex("""\w+: capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (-?\d+)""")

    private fun parseIngredients(input: String) = INGREDIENT_REGEX
        .findAll(input)
        .map { ingredient ->
            val (capacity, durability, flavor, texture, calories) = ingredient.destructured
            Ingredient(capacity.toInt(), durability.toInt(), flavor.toInt(), texture.toInt(), calories.toInt())
        }
        .toList()

    // short variable names:
    // c = capacity; d = durability; f = flavor; t = texture; cal = calories
    // if these are followed by a number x, it's a property per single teaspoon of ingredient x (ing<x>)
    // otherwise it's a property of a whole cookie using n<x> teaspoons of ingredient x

    private fun score(c: Int, d: Int, f: Int, t: Int) = if (c <= 0 || d <= 0 || f <= 0 || t <= 0) 0 else c * d * f * t

    private fun maxScore(ing1: Ingredient, ing2: Ingredient): Int {
        val (c1, d1, f1, t1) = ing1
        val (c2, d2, f2, t2) = ing2
        var maxScore = 0
        for (n1 in 0..100) {
            val n2 = 100 - n1
            val c = n1 * c1 + n2 * c2
            val d = n1 * d1 + n2 * d2
            val f = n1 * f1 + n2 * f2
            val t = n1 * t1 + n2 * t2
            val score = score(c, d, f, t)
            if (score > maxScore) maxScore = score
        }
        return maxScore
    }

    private fun maxScore(ing1: Ingredient, ing2: Ingredient, ing3: Ingredient, ing4: Ingredient): Int {
        val (c1, d1, f1, t1) = ing1
        val (c2, d2, f2, t2) = ing2
        val (c3, d3, f3, t3) = ing3
        val (c4, d4, f4, t4) = ing4
        var maxScore = 0
        for (n1 in 0..100) {
            for (n2 in 0..100 - n1) {
                for (n3 in 0..100 - n1 - n2) {
                    val n4 = 100 - n1 - n2 - n3
                    val c = n1 * c1 + n2 * c2 + n3 * c3 + n4 * c4
                    val d = n1 * d1 + n2 * d2 + n3 * d3 + n4 * d4
                    val f = n1 * f1 + n2 * f2 + n3 * f3 + n4 * f4
                    val t = n1 * t1 + n2 * t2 + n3 * t3 + n4 * t4
                    val score = score(c, d, f, t)
                    if (score > maxScore) maxScore = score
                }
            }
        }
        return maxScore
    }

    private fun maxScoreWith500Calories(ing1: Ingredient, ing2: Ingredient): Int {
        val (c1, d1, f1, t1, cal1) = ing1
        val (c2, d2, f2, t2, cal2) = ing2
        var maxScore = 0
        for (n1 in 0..100) {
            val n2 = 100 - n1
            val cal = n1 * cal1 + n2 * cal2
            if (cal == 500) {
                val c = n1 * c1 + n2 * c2
                val d = n1 * d1 + n2 * d2
                val f = n1 * f1 + n2 * f2
                val t = n1 * t1 + n2 * t2
                val score = score(c, d, f, t)
                if (score > maxScore) maxScore = score
            }
        }
        return maxScore
    }

    private fun maxScoreWith500Calories(ing1: Ingredient, ing2: Ingredient, ing3: Ingredient, ing4: Ingredient): Int {
        val (c1, d1, f1, t1, cal1) = ing1
        val (c2, d2, f2, t2, cal2) = ing2
        val (c3, d3, f3, t3, cal3) = ing3
        val (c4, d4, f4, t4, cal4) = ing4
        var maxScore = 0
        for (n1 in 0..100) {
            for (n2 in 0..100 - n1) {
                for (n3 in 0..100 - n1 - n2) {
                    val n4 = 100 - n1 - n2 - n3
                    val cal = n1 * cal1 + n2 * cal2 + n3 * cal3 + n4 * cal4
                    if (cal == 500) {
                        val c = n1 * c1 + n2 * c2 + n3 * c3 + n4 * c4
                        val d = n1 * d1 + n2 * d2 + n3 * d3 + n4 * d4
                        val f = n1 * f1 + n2 * f2 + n3 * f3 + n4 * f4
                        val t = n1 * t1 + n2 * t2 + n3 * t3 + n4 * t4
                        val score = score(c, d, f, t)
                        if (score > maxScore) maxScore = score
                    }
                }
            }
        }
        return maxScore
    }

    private inline fun maxScore(
        input: String,
        with2Ingredients: (Ingredient, Ingredient) -> Int,
        with4Ingredients: (Ingredient, Ingredient, Ingredient, Ingredient) -> Int,
    ): Int {
        val ingredients = parseIngredients(input)
        return when (ingredients.size) {
            2 -> {
                val (ing1, ing2) = ingredients
                with2Ingredients(ing1, ing2)
            }
            4 -> {
                val (ing1, ing2, ing3, ing4) = ingredients
                with4Ingredients(ing1, ing2, ing3, ing4)
            }
            else -> illegalInput(input)
        }
    }

    override fun part1(input: String) = maxScore(input, ::maxScore, ::maxScore)

    override fun part2(input: String) = maxScore(input, ::maxScoreWith500Calories, ::maxScoreWith500Calories)
}
