package util

fun Regex.match(input: CharSequence) =
    matchEntire(input)?.destructured ?: throw IllegalArgumentException("'$input' does not match '$this'")
