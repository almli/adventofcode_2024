package dag8.del2
import java.io.File
import kotlin.math.abs

lateinit var data: Array<CharArray>
var maxX: Int = 0
var maxY: Int = 0
var found = mutableListOf<Pair<Int, Int>>()

fun main() {
    data = File("../data.txt").readLines().map { it.toCharArray() }.toTypedArray()
    maxY = data.size
    maxX = data[0].size

    val antennaMap = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()
    data.forEachIndexed { lineIndex, line ->
        line.withIndex()
        .groupBy({ it.value }, { it.index })
        .forEach { (ant, indexList) ->
            if (ant != '.') {
                 antennaMap.getOrPut(ant) { mutableListOf() }.addAll(indexList.map { Pair(it, lineIndex) })
            }
        }
    }
    
    antennaMap.forEach { (a, aLoc) ->
       collectAntinodes(findAntennaPairs(aLoc))
    }
    println(found.size)
}
fun collectAntinodes(antennaPairs: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>) {
    antennaPairs.forEach { (a, b) ->
        data.forEachIndexed { lineIndex, line ->
            line.withIndex().forEach { (charIndex, char) ->
                val node = Pair(charIndex, lineIndex)
                if (node !in found && isInLine(node, a, b)) {
                    found.add(node)
                }
            }
        }
    }
}

fun isInLine(node: Pair<Int, Int>, a: Pair<Int, Int>, b: Pair<Int, Int>): Boolean {
    val (aX, aY) = a
    val (bX, bY) = b
    val (x, y) = node
    val dx = aX - bX
    val dy = aY - bY
    val der = dy.toDouble() / dx
    val derivedY = (dy.toDouble() / dx) * (x-aX) + aY
    if ((dy * (x-aX)) % dx == 0) {
        return derivedY.toInt() == y
    }
    return false
}

fun isInBounds(node:Pair<Int,Int>): Boolean {
    return node.first >= 0 && node.first < maxX && node.second >= 0 && node.second < maxY
}

fun findAntennaPairs(aLoc: List<Pair<Int, Int>>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    val antennaPairs = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
    aLoc.forEach { a ->
        aLoc.forEach { b ->
            if (a != b) {
                val pair = Pair(a, b)
                val reversedPair = Pair(b, a)
                if (!antennaPairs.contains(reversedPair)) {
                    antennaPairs.add(pair)
                }
            }
        }
    }
    return antennaPairs
}

 