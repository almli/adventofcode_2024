package dag20.del1

import java.io.File

data class Path(val cost: Int, val steps: List<Step>)
data class Step(val dir: Char, val pos: Pair<Int, Int>, val cost: Int)

data class Cheat(val steps: Pair<Pair<Int, Int>, Pair<Int, Int>>, val savings: Int)

val mazePosCostMap = mutableMapOf<Pair<Int, Int>, Int>()
var startPos = -1 to -1
var endPos = -1 to -1

var CHEAT_MARGIN = 0

var MAX_CHEAT = 2

var posToEndMap = mutableMapOf<Pair<Int, Int>, Int>()
var bestPathCost = 0

fun main() {
    val maze = File("../data.txt").readLines().map { it.toCharArray() }.toTypedArray()
    startPos = findChar(maze, 'S')
    endPos = findChar(maze, 'E')
    dump(maze, mutableListOf())

    var bestPath = minOf(
        tryMove(startPos, maze, '>', mutableListOf(), 0),
        tryMove(startPos, maze, 'v', mutableListOf(), 0),
        tryMove(startPos, maze, '^', mutableListOf(), 0),
        tryMove(startPos, maze, '<', mutableListOf(), 0)
    ) { p1, p2 -> p1.cost.compareTo(p2.cost) }

    bestPathCost = bestPath.cost
    println(bestPath)

    val stepsCount = bestPath.steps.size
    for (i in bestPath.steps.indices) {
        posToEndMap[bestPath.steps[i].pos] = stepsCount - i
    }

    val cheatMap = mutableMapOf<Pair<Pair<Int, Int>, Pair<Int, Int>>, Cheat>()
    collectCheats(startPos, maze, '>', mutableListOf(), 0, false, cheatMap)
    collectCheats(startPos, maze, '<', mutableListOf(), 0, false, cheatMap)
    collectCheats(startPos, maze, '^', mutableListOf(), 0, false, cheatMap)
    collectCheats(startPos, maze, 'v', mutableListOf(), 0, false, cheatMap)
    collectCheats(startPos, maze, '>', mutableListOf(), 1, false, cheatMap)
    collectCheats(startPos, maze, '<', mutableListOf(), 1, false, cheatMap)
    collectCheats(startPos, maze, '^', mutableListOf(), 1, false, cheatMap)
    collectCheats(startPos, maze, 'v', mutableListOf(), 1, false, cheatMap)
//9356
    val cheats = cheatMap.values.filter { it.savings >= CHEAT_MARGIN }.sortedBy { it.savings }

    for (cheat in cheats) {
        print("saving: ${cheat.savings}\n")
        dump(maze, listOf(Step('1', cheat.steps.first, 0), Step('2', cheat.steps.second, 0)).toMutableList())
    }
    val map = cheats.groupingBy { it.savings }.eachCount()
    println(map)
    val ant = cheats.size
    println("ant: $ant")
}

fun findChar(maze: Array<CharArray>, c: Char): Pair<Int, Int> {
    for (i in 0..<maze.size) for (j in 0..<maze[i].size) if (maze[i][j] == c) return j to i
    throw RuntimeException("$c mangler")
}

fun tryMove(pos: Pair<Int, Int>, maze: Array<CharArray>, dir: Char, visited: MutableList<Step>, accCost: Int): Path {
    val nextPos = nextPos(dir, pos)
    if (isEdge(nextPos, maze)) return Path(Int.MAX_VALUE, emptyList())
    if (!isInside(nextPos, maze, false)) return Path(Int.MAX_VALUE, emptyList())

    if (visited.find { it.pos == nextPos } != null) return Path(Int.MAX_VALUE, emptyList())
    val costSoFar = accCost + 1
    val step = Step(dir, nextPos, costSoFar)
    visited.add(step)
    if (nextPos == endPos) {
        return Path(costSoFar, visited)
    }
    val posCost = mazePosCostMap.getOrPut(nextPos) { costSoFar }
    if (costSoFar <= posCost) {
        mazePosCostMap[nextPos] = costSoFar
    } else {
        return Path(Int.MAX_VALUE, emptyList())
    }
    return minOf(
        tryMove(nextPos, maze, dir, visited.toMutableList(), costSoFar),
        tryMove(nextPos, maze, left(dir), visited.toMutableList(), costSoFar),
        tryMove(nextPos, maze, right(dir), visited.toMutableList(), costSoFar)
    ) { p1, p2 -> p1.cost.compareTo(p2.cost) }
}

fun collectCheats(
    pos: Pair<Int, Int>,
    maze: Array<CharArray>,
    dir: Char,
    visited: MutableList<Step>,
    cheatLevel: Int,
    cheatDone: Boolean,
    cheatMap: MutableMap<Pair<Pair<Int, Int>, Pair<Int, Int>>, Cheat>
) {

    val nextPos = nextPos(dir, pos)
    if (isEdge(nextPos, maze)) return
    var inCheat = cheatLevel in 1..<MAX_CHEAT;

    if (!isInside(nextPos, maze, inCheat)) return
    val cellValue = maze[nextPos.second][nextPos.first]
    if (cheatLevel == 1 && cellValue != '#') return
    if (inCheat && cellValue != '#') {
        inCheat = false
    }
    if (visited.find { it.pos == nextPos } != null) return
    val costSoFar = visited.size + 1
    val step = Step(if (cheatLevel == 1) '1' else if (inCheat) '*' else dir, nextPos, costSoFar)
    visited.add(step)

    if (costSoFar > bestPathCost - CHEAT_MARGIN) return

    if (nextPos == endPos) {
        val cheat = buildCheat(visited, costSoFar) ?: return
        val currentCheat = cheatMap.getOrPut(cheat.steps) { cheat }
        if (currentCheat.savings > cheat.savings)
            cheatMap[currentCheat.steps] = cheat
        return
    }

    if ((cheatDone || inCheat) && posToEndMap[nextPos] != null) {
        val cheat = buildCheat(visited, posToEndMap[nextPos]!! + costSoFar - 1) ?: return
        val currentCheat = cheatMap.getOrPut(cheat.steps) { cheat }
        if (currentCheat.savings > cheat.savings)
            cheatMap[currentCheat.steps] = cheat
        return
    }
    val nextCheatDone = cheatDone || cheatLevel == MAX_CHEAT
    val nextCheatLevel = if (inCheat && cheatLevel < MAX_CHEAT) cheatLevel + 1 else 0

    if (nextCheatLevel > 0) {
        collectCheats(nextPos, maze, dir, visited.toMutableList(), nextCheatLevel, nextCheatDone, cheatMap)
        collectCheats(nextPos, maze, left(dir), visited.toMutableList(), nextCheatLevel, nextCheatDone, cheatMap)
        collectCheats(nextPos, maze, right(dir), visited.toMutableList(), nextCheatLevel, nextCheatDone, cheatMap)
    } else if (nextCheatDone) {
        collectCheats(nextPos, maze, dir, visited.toMutableList(), 0, true, cheatMap)
        collectCheats(nextPos, maze, left(dir), visited.toMutableList(), 0, true, cheatMap)
        collectCheats(nextPos, maze, right(dir), visited.toMutableList(), 0, true, cheatMap)
    } else {
        collectCheats(nextPos, maze, dir, visited.toMutableList(), 0, false, cheatMap)
        collectCheats(nextPos, maze, left(dir), visited.toMutableList(), 0, false, cheatMap)
        collectCheats(nextPos, maze, right(dir), visited.toMutableList(), 0, false, cheatMap)
        collectCheats(nextPos, maze, dir, visited.toMutableList(), 1, false, cheatMap)
        collectCheats(nextPos, maze, left(dir), visited.toMutableList(), 1, false, cheatMap)
        collectCheats(nextPos, maze, right(dir), visited.toMutableList(), 1, false, cheatMap)
    }
}

fun buildCheat(visited: MutableList<Step>, costSoFar: Int): Cheat? {
    val first = visited.find { it.dir == '1' }?.pos
    var second = visited.findLast { it.dir == '*' }?.pos
    if (second == null) second = visited.last().pos
    if (first == null || second == null)
        return null;
    if (bestPathCost - costSoFar <= 0)
        return null
    return Cheat(first to second, bestPathCost - costSoFar)
}

fun isInside(pos: Pair<Int, Int>, maze: Array<CharArray>, cheat: Boolean): Boolean {
    return pos.second >= 0 && pos.second < maze.size && pos.first >= 0 && pos.first < maze[pos.second].size && (maze[pos.second][pos.first] != '#' || cheat)
}

fun isEdge(pair: Pair<Int, Int>, arrays: Array<CharArray>): Boolean {
    return pair.first == 0 || pair.second == 0 || pair.first == arrays[0].size - 1 || pair.second == arrays.size - 1
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

fun nextPos(dir: Char, pos: Pair<Int, Int>): Pair<Int, Int> {
    return when (dir) {
        '>' -> pos.first + 1 to pos.second
        '<' -> pos.first - 1 to pos.second
        '^' -> pos.first to pos.second - 1
        'v' -> pos.first to pos.second + 1
        else -> throw RuntimeException("Ugyldig retning : $dir")
    }
}

fun dump(maze: Array<CharArray>, steps: MutableList<Step>) {
    println("-----------------")
    val mazeCopy = maze.map { it.copyOf() }.toTypedArray()
    for (step in steps) {
        mazeCopy[step.pos.second][step.pos.first] = step.dir
    }
    println(mazeCopy.joinToString("\n") { it.concatToString() })
}