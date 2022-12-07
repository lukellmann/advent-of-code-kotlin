package util

fun Regex.match(input: CharSequence) = matchEntire(input)!!.destructured
