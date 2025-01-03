package dag21

import dag14.del2.SECONDS
import java.io.File

fun path(from: Char, to: Char, directions: String): Triple<Char, Char, String> {
    return Triple(from, to, directions)
}

val rootNavPaths = mutableListOf<Triple<Char, Char, String>>().apply {
    addAll(
        listOf(
            path('A', '0', "<"),
            path('A', '3', "^"),
            path('0', 'A', ">"),
            path('0', '2', "^"),
            path('1', '2', ">"),
            path('1', '4', "^"),
            path('2', '1', "<"),
            path('2', '5', "^"),
            path('2', '3', ">"),
            path('2', '0', "v"),
            path('3', '2', "<"),
            path('3', '6', "^"),
            path('3', 'A', "v"),
            path('4', '7', "^"),
            path('4', '5', ">"),
            path('4', '1', "v"),
            path('5', '4', "<"),
            path('5', '8', "^"),
            path('5', '6', ">"),
            path('5', '2', "v"),
            path('6', '5', "<"),
            path('6', '9', "^"),
            path('6', '3', "v"),
            path('7', '8', ">"),
            path('7', '4', "v"),
            path('8', '9', ">"),
            path('8', '5', "v"),
            path('8', '7', "<"),
            path('9', '6', "v"),
            path('9', '8', "<")
        )
    )

}

val dirNavPath = mutableListOf<Triple<Char, Char, String>>().apply {
    addAll(
        listOf(
            path('<', 'v', ">"),
            path('v', '<', "<"),
            path('v', '^', "^"),
            path('v', '>', ">"),
            path('^', 'A', ">"),
            path('^', 'v', "v"),
            path('A', '^', "<"),
            path('A', '>', "v"),
            path('>', 'A', "^"),
            path('>', 'v', "<")
        )
    )
}

fun findPath(
    map: List<Triple<Char, Char, String>>,
    fromKey: Char,
    toKey: Char,
    visited: MutableList<Char>,
    path: String
): Set<String> {
    if (fromKey == toKey) {
        return setOf(path)
    }
    if (visited.contains(fromKey))
        return setOf()

    visited.add(fromKey)
    val candidatePaths = hashSetOf<String>()
    for (tr in map.filter { it.first == fromKey }) {
        candidatePaths.addAll(findPath(map, tr.second, toKey, visited.toMutableList(), path + tr.third))
    }

    if (candidatePaths.isEmpty())
        return setOf()
    val shortest = candidatePaths.minBy { it.length }.length
    return candidatePaths.filter { it.length == shortest }.toSet()

}

fun rootPath(c1: Char, c2: Char): Set<String> {
    return findPath(rootNavPaths, c1, c2, mutableListOf(), "")
}


fun dirPath(c1: Char, c2: Char): Set<String> {
    return findPath(dirNavPath, c1, c2, mutableListOf(), "")
}

fun combinations(sets: List<Set<String>>): List<String> {
    if (sets.isEmpty()) return emptyList()

    return sets.fold(listOf("")) { acc, set ->
        acc.flatMap { prefix -> set.map { element -> "$prefix$element" } }
    }
}

fun pathAlternatives(code: CharArray, pathFunction: (Char, Char) -> Set<String>): List<String> {
    val paths = mutableListOf<Set<String>>()
    var fromChar = 'A'
    for (c in code) {
        paths.add(pathFunction(fromChar, c))
        paths.add(setOf("A"))
        fromChar = c
    }
    val all = combinations(paths)
    if (all.isEmpty())
        return mutableListOf()
    val shortest = all.minBy { it.length }.length
    return all.filter { it.length == shortest }

}

fun main() {
    val codes = File("./data.txt").readLines()
    var sum = 0
    for (code in codes) {
        val firstRobotPaths = pathAlternatives(code.toCharArray(), ::rootPath)
        println(firstRobotPaths)
        var currentPaths = firstRobotPaths
        for (i in 1 until 3) {
            var nextPaths = mutableListOf<String>()
            for (path in currentPaths) {
                nextPaths.addAll(pathAlternatives(path.toCharArray(), ::dirPath))
            }
            val minPath = nextPaths.minBy { it.length }.length
            currentPaths = nextPaths.filter { it.length==minPath }
        }
        val num = code.substring(0, 3).toInt()
        val length =currentPaths.first().length
        val codeSum = num * length
        println("$num, $length, $codeSum")
        sum += codeSum
    }
    println(sum)
}
