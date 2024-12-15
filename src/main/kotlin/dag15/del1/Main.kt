package dag15.del1

import java.io.File

fun main() {
    val data = File("../data.txt").readLines().map { it.toCharArray() }
    val splitIndex = data.indexOfFirst { it.isEmpty() }
    val wh = data.subList(0, splitIndex).toTypedArray()
    val moves = data.drop(splitIndex + 1).flatMap { it.asIterable() }.toCharArray()
    println(wh.joinToString("\n") { it.concatToString() })
    var pos = findRobot(wh)
    wh[pos.second][pos.first] = '.'
    val navigators = mapOf(
        '<' to { x: Int, y: Int -> x - 1 to y },
        '>' to { x: Int, y: Int -> x + 1 to y },
        '^' to { x: Int, y: Int -> x to y - 1 },
        'v' to { x: Int, y: Int -> x to y + 1 }
    )
    for (move in moves) {
        val navigator = navigators[move]!!
        val path = findPath(navigator, pos.first to pos.second, wh)
        val bump = bump(path)
        if (bump.isNotEmpty() && bump[0] == '.') {
            updatePath(bump, navigator, pos.first to pos.second, wh)
            pos = navigator.invoke(pos.first, pos.second);
        }
    }
    val sum = sumOfGps(wh)
    wh[pos.second][pos.first] = '@'
    println(wh.joinToString("\n") { it.concatToString() })
    println(sum)
}
fun sumOfGps(wh: Array<CharArray>): Int {
    var sum = 0
    for (i in 0..<wh.size)
        for (j in 0..<wh[i].size) {
            if (wh[i][j] != '#' && wh[i][j] != '.') {
                val gps = 100 * i + j
                sum += gps
            }
        }
    return sum
}
fun updatePath(path: CharArray, navigator: ((Int, Int) -> Pair<Int, Int>), pos: Pair<Int, Int>, wh: Array<CharArray>) {
    var nextPos = pos;
    for (c in path) {
        nextPos = navigator.invoke(nextPos.first, nextPos.second)
        wh[nextPos.second][nextPos.first] = c
    }
}
fun isInside(pos: Pair<Int, Int>, wh: Array<CharArray>): Boolean {
    return pos.second >= 0 && pos.second < wh.size && pos.first >= 0 && pos.first < wh[pos.second].size
}
fun findPath(navigator: ((Int, Int) -> Pair<Int, Int>), pos: Pair<Int, Int>, wh: Array<CharArray>): CharArray {
    val res = mutableListOf<Char>()
    var nextPos = pos;
    while (true) {
        nextPos = navigator.invoke(nextPos.first, nextPos.second)
        if (!isInside(nextPos, wh) || wh[nextPos.second][nextPos.first] == '#')
            break
        res.add(wh[nextPos.second][nextPos.first])
    }
    return res.toCharArray()
}
fun bump(path: CharArray): CharArray {
    if (path.isEmpty() || path[0] == '.') return path
    val firstSpaceIndex = path.indexOf('.')
    if (firstSpaceIndex < 0) return path
    val zeroes = CharArray(firstSpaceIndex) { 'O' }
    val rest = path.copyOfRange(firstSpaceIndex + 1, path.size)
    return charArrayOf('.') + zeroes + rest
}
fun findRobot(wh: Array<CharArray>): Pair<Int, Int> {
    for (i in 0..<wh.size)
        for (j in 0..<wh[i].size)
            if (wh[i][j] == '@')
                return j to i
    throw RuntimeException("robot mangler")
}