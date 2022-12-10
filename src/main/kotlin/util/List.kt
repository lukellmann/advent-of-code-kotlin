package util

fun <T> List<T>.permutations(): Sequence<List<T>> {
    val me = this
    if (me.size <= 1) return sequenceOf(me)
    return sequence {
        for (perm in me.subList(1, me.size).permutations()) {
            for (i in me.indices) {
                yield(perm.subList(0, i) + me[0] + perm.subList(i, perm.size))
            }
        }
    }
}
