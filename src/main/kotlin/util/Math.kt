package util

fun Int.pow(n: Int) = toBigInteger().pow(n).intValueExact()
fun Long.pow(n: Int) = toBigInteger().pow(n).longValueExact()
