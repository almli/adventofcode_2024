package dag19

import java.io.File

var foundCache = mutableMapOf<String, Long>()

fun main() {
    val data = File("data.txt").readLines()
    var stripes =
        data[0].split(",").map { it.trim() }.sortedWith(compareBy<String> { it[0] }.thenBy { it.length }).toList()
    var patterns = data.drop(2).map { it.trim() }.toList()

    var del1 = 0L;
    var del2 = 0L;
    for (pattern in patterns) {
        val patternOptionCount = findPattern(pattern, stripes)
        if (patternOptionCount > 0) del1++
        del2 += patternOptionCount
        foundCache = mutableMapOf<String, Long>()
    }
    println("Del 1: $del1")
    println("Del 2: $del2")
}

fun findPattern(pattern: String, stripes: List<String>): Long {
    if (pattern.isEmpty()) return 1
    foundCache[pattern]?.let { return it }
    var count = 0L;
    for (stripe in stripes) {
        if (pattern.startsWith(stripe)) {
            val remPattern = pattern.removePrefix(stripe)
            val patternC = foundCache.getOrPut(remPattern) { findPattern(remPattern, stripes) }
            count += patternC
        }
    }
    return count
}
