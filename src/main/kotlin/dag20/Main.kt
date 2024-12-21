package dag20

import java.io.File

data class Path(val cost: Int, val steps: List<Step>)
data class Step(val dir: Char, val pos: Pair<Int, Int>, val cost: Int)


val mazePosCostMap = mutableMapOf<Pair<Int, Int>, Int>()
var allPaths = mutableListOf<Path>()
var startPos = -1 to -1
var endPos = -1 to -1
//var BEST = 84
var CHEAT_MARGIN = 100

var BEST = 9356
fun main() {
    val maze = File("./data.txt").readLines().map { it.toCharArray() }.toTypedArray()
    startPos = findChar(maze, 'S')
    endPos = findChar(maze, 'E')
    dump(maze, mutableListOf())

    var bestCheat = minOf(
        tryMove(startPos, maze, '>', mutableListOf(), 0, 0, false),
        tryMove(startPos, maze, 'v', mutableListOf(), 0, 0, false),
        tryMove(startPos, maze, '^', mutableListOf(), 0, 0, false),
        tryMove(startPos, maze, '<', mutableListOf(), 0, 0, false),
        tryMove(startPos, maze, '>', mutableListOf(), 0, 1, false),
        tryMove(startPos, maze, 'v', mutableListOf(), 0, 1, false),
        tryMove(startPos, maze, '^', mutableListOf(), 0, 1, false),
        tryMove(startPos, maze, '<', mutableListOf(), 0, 1, false)
    ) { p1, p2 -> p1.cost.compareTo(p2.cost) }
//9356

    allPaths = allPaths.distinctBy { path -> path.steps.map { it.pos } }.toMutableList()
    val cheatCosts = allPaths.map { it.cost }.sorted()
    println(cheatCosts)

    for (p in allPaths) {
        println(p.steps.map { it.pos })
        //println(p.cost)
        // dump(maze, p.steps.toMutableList())
    }
    // dump(maze, best.steps.toMutableList())
    // println("best  :" + best.cost)
    println("best cheat  :" + bestCheat.cost)

    var ant = allPaths.distinct().size - 1
    println("ant: $ant")
}

fun right(ch: Char): Char {
    return when (ch) {
        '>' -> 'v'
        'v' -> '<'
        '<' -> '^'
        '^' -> '>'
        else -> throw RuntimeException("Ugyldig retning")
    }
}

fun left(ch: Char): Char {
    return when (ch) {
        '>' -> '^'
        'v' -> '>'
        '<' -> 'v'
        '^' -> '<'
        else -> throw RuntimeException("Ugyldig retning")
    }
}

fun findChar(maze: Array<CharArray>, c: Char): Pair<Int, Int> {
    for (i in 0..<maze.size) for (j in 0..<maze[i].size) if (maze[i][j] == c) return j to i
    throw RuntimeException("$c mangler")
}

fun tryMove(
    pos: Pair<Int, Int>,
    maze: Array<CharArray>,
    dir: Char,
    visited: MutableList<Step>,
    accCost: Int,
    cheatLevel: Int,
    cheatDone: Boolean
): Path {

    val nextPos = when (dir) {
        '>' -> pos.first + 1 to pos.second
        '<' -> pos.first - 1 to pos.second
        '^' -> pos.first to pos.second - 1
        'v' -> pos.first to pos.second + 1
        else -> throw RuntimeException("Ugyldig retning")
    }
    if (isEdge(nextPos, maze)) return Path(Int.MAX_VALUE, emptyList())
    val inCheat = cheatLevel > 0;
    if (!isInside(nextPos, maze, inCheat)) return Path(Int.MAX_VALUE, emptyList())

    if (cheatLevel == 1 && maze[nextPos.second][nextPos.first] != '#') return Path(Int.MAX_VALUE, emptyList())

    val nextCheatDone = cheatDone || cheatLevel == 2

    val costSoFar = accCost + 1
    if (costSoFar > BEST || ((inCheat || cheatDone) && costSoFar >= BEST - CHEAT_MARGIN)) return Path(
        Int.MAX_VALUE,
        emptyList()
    )

    if (visited.find { it.pos == nextPos } != null) return Path(Int.MAX_VALUE, emptyList())
    val step = Step(if (inCheat) cheatLevel.digitToChar() else dir, nextPos, costSoFar)
    visited.add(step)

    var nextCheatlevel = when (cheatLevel) {
        1 -> 2
        else -> 0
    }
    if (nextPos == endPos) {
        val p = Path(costSoFar, visited)
        allPaths.add(p)
        return p
    }

    if (!cheatDone && !inCheat) {
        val posCost = mazePosCostMap.getOrPut(nextPos) { costSoFar }
        if (posCost == null || costSoFar <= posCost) {
            mazePosCostMap[nextPos] = costSoFar
        } else {
            return Path(Int.MAX_VALUE, emptyList())
        }
    } else {
        val posCost = mazePosCostMap.get(nextPos)
        if (posCost != null && costSoFar > posCost) {
            return Path(Int.MAX_VALUE, emptyList())
        }
    }

    if (inCheat || cheatDone) {
        return minOf(
            tryMove(nextPos, maze, dir, visited.toMutableList(), costSoFar, nextCheatlevel, nextCheatDone),
            tryMove(nextPos, maze, left(dir), visited.toMutableList(), costSoFar, nextCheatlevel, nextCheatDone),
            tryMove(nextPos, maze, right(dir), visited.toMutableList(), costSoFar, nextCheatlevel, nextCheatDone)
        ) { p1, p2 -> p1.cost.compareTo(p2.cost) }
    } else {
        return minOf(
            tryMove(nextPos, maze, dir, mutableListOf(), costSoFar, 0, false),
            tryMove(nextPos, maze, left(dir), visited.toMutableList(), costSoFar, 0, false),
            tryMove(nextPos, maze, right(dir), visited.toMutableList(), costSoFar, 0, false),
            tryMove(nextPos, maze, dir, visited.toMutableList(), costSoFar, 1, false),
            tryMove(nextPos, maze, left(dir), visited.toMutableList(), costSoFar, 1, false),
            tryMove(nextPos, maze, right(dir), visited.toMutableList(), costSoFar, 1, false)
        ) { p1, p2 -> p1.cost.compareTo(p2.cost) }
    }
}

fun isInside(pos: Pair<Int, Int>, maze: Array<CharArray>, cheat: Boolean): Boolean {
    return pos.second >= 0 && pos.second < maze.size && pos.first >= 0 && pos.first < maze[pos.second].size && (maze[pos.second][pos.first] != '#' || cheat)
}

fun isEdge(pair: Pair<Int, Int>, arrays: Array<CharArray>): Boolean {
    return pair.first == 0 || pair.second == 0 || pair.first == arrays[0].size - 1 || pair.second == arrays.size - 1
}

fun dump(maze: Array<CharArray>, steps: MutableList<Step>) {
    println("-----------------")
    val mazeCopy = maze.map { it.copyOf() }.toTypedArray()
    for (step in steps) {
        mazeCopy[step.pos.second][step.pos.first] = step.dir
    }
    println(mazeCopy.joinToString("\n") { it.concatToString() })
}