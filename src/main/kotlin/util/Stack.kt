package util

typealias Stack<E> = ArrayDeque<E>

fun <E> Stack<E>.peek() = last()
fun <E> Stack<E>.pop() = removeLast()
fun <E> Stack<E>.push(element: E) = addLast(element)
fun <E> Stack<E>.pushFromBottom(element: E) = addFirst(element)
