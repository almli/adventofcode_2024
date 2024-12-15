package dag15.del2

import java.io.File

fun main() {
    val data = File("../data.txt").readLines()
    val splitIndex = data.indexOfFirst { it.isEmpty() }
    val wh = data.subList(0, splitIndex)
        .map { it.replace("O", "[]").replace(".", "..").replace("#", "##").replace("@", "@.").toCharArray() }
        .toTypedArray()
    val moves = data.drop(splitIndex + 1).flatMap { it.asIterable() }.toCharArray()
    dump(wh)
    var pos = findRobot(wh)
    wh[pos.second][pos.first] = '.'
    val navigators = mapOf(
        '<' to { x: Int, y: Int -> x - 1 to y },
        '>' to { x: Int, y: Int -> x + 1 to y },
        '^' to { x: Int, y: Int -> x to y - 1 },
        'v' to { x: Int, y: Int -> x to y + 1 })
    for (move in moves) {
        val navigator = navigators[move]!!
        if (isEmpty(pos, navigator, wh)) {
            pos = navigator.invoke(pos.first, pos.second);
        } else {
            val objectPositions = mutableListOf<Pair<Int, Int>>()
            findObjectFromPos(pos, navigator, wh, objectPositions, move == '^' || move == 'v')
            if (!objectPositions.isEmpty() && isMoveable(objectPositions, navigator, wh)) {
                updateObjectPositions(navigator, objectPositions, wh)
                pos = navigator.invoke(pos.first, pos.second);
            }
        }
        wh[pos.second][pos.first] = '@'
        wh[pos.second][pos.first] = '.'
    }
    val sum = sumOfGps(wh)
    wh[pos.second][pos.first] = '@'
    println(sum)
}

private fun dump(wh: Array<CharArray>) {
    println(wh.joinToString("\n") { it.concatToString() })
}

fun updateObjectPositions(
    navigator: (Int, Int) -> Pair<Int, Int>, objectPositions: MutableList<Pair<Int, Int>>, wh: Array<CharArray>
) {
    val posMap = objectPositions.map { it to wh[it.second][it.first] }.toMap()
    for (pos in objectPositions) {
        wh[pos.second][pos.first] = '.'
    }
    for (pos in objectPositions) {
        val nextPos = navigator.invoke(pos.first, pos.second)
        wh[nextPos.second][nextPos.first] = posMap[pos]!!
    }
}
fun isMoveable(
    objectPositions: List<Pair<Int, Int>>, navigator: (Int, Int) -> Pair<Int, Int>, wh: Array<CharArray>
): Boolean {
    for (pos in objectPositions) {
        val nextPos = navigator.invoke(pos.first, pos.second)
        if (!isInside(nextPos, wh) || wh[nextPos.second][nextPos.first] == '#') {
            return false
        }
    }
    return true
}
fun isEmpty(pair: Pair<Int, Int>, function: (Int, Int) -> Pair<Int, Int>, arrays: Array<CharArray>): Boolean {
    val (x, y) = function.invoke(pair.first, pair.second)
    return arrays[y][x] == '.'
}
fun findObjectFromPos(
    currentPos: Pair<Int, Int>,
    navigator: (Int, Int) -> Pair<Int, Int>,
    wh: Array<CharArray>,
    objectPositions: MutableList<Pair<Int, Int>>,
    vertical: Boolean
) {
    val nextPos = navigator.invoke(currentPos.first, currentPos.second)
    if (!isInside(nextPos, wh) || wh[nextPos.second][nextPos.first] == '#') {
        return
    }
    val c = wh[nextPos.second][nextPos.first];
    if (c == '[') {
        objectPositions.add(nextPos)
        findObjectFromPos(nextPos, navigator, wh, objectPositions, vertical)
        if (vertical) {
            objectPositions.add(nextPos.first + 1 to nextPos.second)
            findObjectFromPos(nextPos.first + 1 to nextPos.second, navigator, wh, objectPositions, vertical)
        }
    } else if (c == ']') {
        objectPositions.add(nextPos)
        findObjectFromPos(nextPos, navigator, wh, objectPositions, vertical)
        if (vertical) {
            objectPositions.add(nextPos.first - 1 to nextPos.second)
            findObjectFromPos(nextPos.first - 1 to nextPos.second, navigator, wh, objectPositions, vertical)
        }
    }
    return
}
fun sumOfGps(wh: Array<CharArray>): Int {
    var sum = 0
    for (i in 0..<wh.size) for (j in 0..<wh[i].size) {
        if (wh[i][j] == '[') {
            sum += 100 * i + j
        }
    }
    return sum
}
fun isInside(pos: Pair<Int, Int>, wh: Array<CharArray>): Boolean {
    return pos.second >= 0 && pos.second < wh.size && pos.first >= 0 && pos.first < wh[pos.second].size
}
fun findRobot(wh: Array<CharArray>): Pair<Int, Int> {
    for (i in 0..<wh.size) for (j in 0..<wh[i].size) if (wh[i][j] == '@') return j to i
    throw RuntimeException("robot mangler")
}