package dag25

import dag13.Machine
import dag13.parse
import java.io.File
import kotlin.math.abs


data class Element(val key: Boolean, val col0: Int, val col1: Int, val col2: Int, val col3: Int, val col4: Int)

fun main() {
    val elements = File("data.txt").readText().trim().split("\n\n").map { it.lines() }.map { parse(it) };

    val keys = elements.filter { it.key }
    val locks = elements.filter { !it.key }

    var cnt = 0;
    for (lock in locks) {
        for (key in keys) {
            if (possible(key, lock))
                cnt++
        }
    }

    println("keys: $keys")
    println("locks: $locks")
    println(cnt)
}

fun possible(key: Element, lock: Element): Boolean {
    return !(key.col0 + lock.col0 > 5 ||
            key.col1 + lock.col1 > 5 ||
            key.col2 + lock.col2 > 5 ||
            key.col3 + lock.col3 > 5 ||
            key.col4 + lock.col4 > 5)
}

fun parse(lines: List<String>): Element {
    val key = lines.first() != "#####"
    val col0 = if (key) toValKey(0, lines) else toValLock(0, lines)
    val col1 = if (key) toValKey(1, lines) else toValLock(1, lines)
    val col2 = if (key) toValKey(2, lines) else toValLock(2, lines)
    val col3 = if (key) toValKey(3, lines) else toValLock(3, lines)
    val col4 = if (key) toValKey(4, lines) else toValLock(4, lines)
    return Element(key, col0, col1, col2, col3, col4)
}

fun toValKey(col: Int, lines: List<String>): Int {
    for (i in 0 until 7) {
        if (lines.get(i).get(col) == '#')
            return 6 - i
    }
    return 6
}

fun toValLock(col: Int, lines: List<String>): Int {
    for (i in 0 until 7) {
        if (lines.get(i).get(col) != '#')
            return i - 1
    }
    return 0
}