package util

fun <T> Iterable<T>.isSorted(comparator: Comparator<in T>): Boolean {
    val iter = iterator()
    if (!iter.hasNext()) return true
    var prev = iter.next()
    while (iter.hasNext()) {
        val curr = iter.next()
        if (comparator.compare(prev, curr) > 0) return false
        prev = curr
    }
    return true
}
