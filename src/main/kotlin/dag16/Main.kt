package dag16
import java.io.File

class Path(val cost: Int, val steps: List<Step>)
data class Step(val dir: Char, val pos: Pair<Int, Int>, val cost: Int)
class PosDirCost() {
    val dir = mutableMapOf<Char, Int>()
}

val mazePosCostMap = mutableMapOf<Pair<Int, Int>, PosDirCost>()
val allPaths = mutableListOf<Path>()

fun main() {
    val maze = File("./data.txt").readLines().map { it.toCharArray() }.toTypedArray()
    var startPos = findChar(maze, 'S')

    var best = minOf(
        tryMove(startPos, maze, '>', mutableListOf<Step>(), 1, 0),
        tryMove(startPos, maze, 'v', mutableListOf<Step>(), 1, 1000),
        tryMove(startPos, maze, '^', mutableListOf<Step>(), 1, 1000),
        tryMove(startPos, maze, '<', mutableListOf<Step>(), 1, 2000)
    ) { p1, p2 -> p1.cost.compareTo(p2.cost) }

    val bestTiles = mutableSetOf<Pair<Int, Int>>()
    bestTiles.addAll(best.steps.map { it.pos })
    for (path in allPaths) {
        for (i in path.steps.size - 1 downTo 0) {
            if (best.steps.contains(path.steps[i])) {
                bestTiles.addAll(path.steps.subList(0, i).map { it.pos })
            }
        }
    }
    println("del 1:" + best.cost)
    println("del 2:" + (bestTiles.count() + 2))
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

fun tryMove(pos: Pair<Int, Int>, maze: Array<CharArray>, dir: Char, visited: MutableList<Step>, stepCost: Int, accCost: Int): Path {
    val nextPos = when (dir) {
        '>' -> pos.first + 1 to pos.second
        '<' -> pos.first - 1 to pos.second
        '^' -> pos.first to pos.second - 1
        'v' -> pos.first to pos.second + 1
        else -> throw RuntimeException("Ugyldig retning")
    }
    if (!isInside(nextPos, maze) || !visited.filter { it.pos == nextPos }.isEmpty()) return Path(Int.MAX_VALUE, emptyList())

    val costSoFar = accCost + stepCost
    if (maze[nextPos.second][nextPos.first] == 'E') {
        val path = Path(costSoFar, visited)
        allPaths.add(path)
        return path
    }
    val step = Step(dir, nextPos, costSoFar)
    visited.add(step)
    val posCost = mazePosCostMap.getOrPut(nextPos) { PosDirCost() }
    val currentCost = posCost.dir[dir]
    if (currentCost == null || currentCost >= accCost + stepCost) {
        posCost.dir[dir] = accCost + stepCost
    } else {
        return Path(Int.MAX_VALUE, emptyList())
    }
    return minOf(
        tryMove(nextPos, maze, dir, visited.toMutableList(), 1, costSoFar),
        tryMove(nextPos, maze, left(dir), visited.toMutableList(), 1001, costSoFar),
        tryMove(nextPos, maze, right(dir), visited.toMutableList(), 1001, costSoFar)
    ) { p1, p2 -> p1.cost.compareTo(p2.cost) }
}

fun isInside(pos: Pair<Int, Int>, maze: Array<CharArray>): Boolean {
    return pos.second >= 0 && pos.second < maze.size && pos.first >= 0 && pos.first < maze[pos.second].size && maze[pos.second][pos.first] != '#'
}

fun findChar(maze: Array<CharArray>, c: Char): Pair<Int, Int> {
    for (i in 0..<maze.size)
        for (j in 0..<maze[i].size)
            if (maze[i][j] == c)
                return j to i
    throw RuntimeException("$c mangler")
}

fun dump(maze: Array<CharArray>, steps: MutableList<Step>) {
    println("-----------------")
    val mazeCopy = maze.map { it.copyOf() }.toTypedArray()
    for (step in steps) {
        mazeCopy[step.pos.second][step.pos.first] = step.dir
    }
    println(mazeCopy.joinToString("\n") { it.concatToString() })
}