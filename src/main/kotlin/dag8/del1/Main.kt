package dag8.del1
import java.io.File
import kotlin.math.abs

var maxX: Int = 0
var maxY: Int = 0
var found = mutableListOf<Pair<Int, Int>>()

fun main() {
    val data = File("../data.txt").readLines().map { it.toCharArray() }.toTypedArray()
    maxY = data.size
    maxX = data[0].size

    val antennaMap = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()
    data.forEachIndexed { lineIndex, line ->
        line.withIndex()
        .groupBy({ it.value }, { it.index })
        .forEach { (ant, indexList) ->
            if (ant != '.') {
                    antennaMap.getOrPut(ant) { mutableListOf() }.addAll(indexList.map { Pair(lineIndex, it) })
            }
        }
    }
    
    antennaMap.forEach { (a, aLoc) ->
       collectAntinodes(aLoc)
    }
    println(found.size)
}

fun collectAntinodes(aLoc: List<Pair<Int, Int>>)  {
    aLoc.forEach { a ->
        aLoc.forEach { b ->
            if (a != b) {
                val (aX, aY) = a
                val (bX, bY) = b
                val xDiff = aX - bX
                val yDiff = aY - bY
                var node1 = aX+xDiff to aY+yDiff
                var node2 = bX-xDiff to bY-yDiff
                if(isInBounds(node1)) {
                    if (node1 !in found) {
                        found.add(node1)
                    }
                }
                if(isInBounds(node2)) {
                    if (node2 !in found) {
                        found.add(node2)
                    }
                }
            }
        }
    }
}

fun isInBounds(node:Pair<Int,Int>): Boolean {
    return node.first >= 0 && node.first < maxX && node.second >= 0 && node.second < maxY
}