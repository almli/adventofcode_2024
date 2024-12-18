package dag18.del2

import java.io.File

data class Step(val dir: Char, val pos: Pair<Int, Int>, val cost: Int)
class PosDirCost() {
    val dir = mutableMapOf<Char, Int>()
}

var mazePosCostMap = mutableMapOf<Pair<Int, Int>, PosDirCost>()
var startPos = 0 to 0
var endPos = 70 to 70

fun main() {
    val data = File("../data.txt").readLines()

    for (i in 1024 until data.size) {
        println("Processing $i")
        val bytes = data.take(i).map { line ->
            line.split(",").let { (x, y) -> x.toInt() to y.toInt() }
        }
        var maze = toMaze(71, 71, bytes)
        //dump(maze, mutableListOf())
        var pathExits =
            tryMove(startPos, maze, '>', mutableListOf<Step>(), 1, 0) ||
                    tryMove(startPos, maze, 'v', mutableListOf<Step>(), 1, 0) ||
                    tryMove(startPos, maze, '^', mutableListOf<Step>(), 1, 0) ||
                    tryMove(startPos, maze, '<', mutableListOf<Step>(), 1, 0)

        if (!pathExits) {
            val final = data.get(i - 1)
            println("del 2:  $final")
            break
        }
        mazePosCostMap = mutableMapOf<Pair<Int, Int>, PosDirCost>()
    }
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

fun toMaze(width: Int, height: Int, pairs: List<Pair<Int, Int>>): Array<CharArray> {
    val maze = Array(height) { CharArray(width) { '.' } }
    for ((x, y) in pairs) {
        if (y in 0 until height && x in 0 until width) {
            maze[y][x] = '#'
        }
    }
    return maze
}

fun tryMove(
    pos: Pair<Int, Int>,
    maze: Array<CharArray>,
    dir: Char,
    visited: MutableList<Step>,
    stepCost: Int,
    accCost: Int
): Boolean {
    val nextPos = when (dir) {
        '>' -> pos.first + 1 to pos.second
        '<' -> pos.first - 1 to pos.second
        '^' -> pos.first to pos.second - 1
        'v' -> pos.first to pos.second + 1
        else -> throw RuntimeException("Ugyldig retning")
    }
    if (visited.find { it.pos == nextPos } != null)
        return false
    if (!isInside(nextPos, maze) || !visited.filter { it.pos == nextPos }.isEmpty()) return false

    val costSoFar = accCost + stepCost
    if (nextPos == endPos) {
        return true
    }
    val step = Step(dir, nextPos, costSoFar)
    visited.add(step)
    val posCost = mazePosCostMap.getOrPut(nextPos) { PosDirCost() }
    val currentCost = posCost.dir[dir]
    if (currentCost == null || currentCost > accCost + stepCost) {
        posCost.dir[dir] = accCost + stepCost
    } else {
        return false
    }
    return tryMove(nextPos, maze, dir, visited.toMutableList(), 1, costSoFar) ||
            tryMove(nextPos, maze, left(dir), visited.toMutableList(), 1, costSoFar) ||
            tryMove(nextPos, maze, right(dir), visited.toMutableList(), 1, costSoFar)

}

fun isInside(pos: Pair<Int, Int>, maze: Array<CharArray>): Boolean {
    return pos.second >= 0 && pos.second < maze.size && pos.first >= 0 && pos.first < maze[pos.second].size && maze[pos.second][pos.first] != '#'
}

fun dump(maze: Array<CharArray>, steps: MutableList<Step>) {
    println("-----------------")
    val mazeCopy = maze.map { it.copyOf() }.toTypedArray()
    for (step in steps) {
        mazeCopy[step.pos.second][step.pos.first] = step.dir
    }
    println(mazeCopy.joinToString("\n") { it.concatToString() })
}