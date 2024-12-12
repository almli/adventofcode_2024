package dag12.del1
import java.io.File
 
lateinit var data: Array<CharArray>
val found = mutableSetOf<Pair<Int, Int>>()

fun main() {
    data = File("../data.txt").readLines().map { it.toCharArray() }.toTypedArray()
    var sum = 0

    for (i in data.indices) {
        for (j in data[i].indices) {
            if (found.contains(Pair(j, i))) continue
            val currentRegion = mutableSetOf<Pair<Int, Int>>()
            val borderCount = findRegionFrom(data[i][j], Pair(j, i), currentRegion)
            found.addAll(currentRegion)
            sum += (borderCount * currentRegion.size)
        }
    }

    println(sum)
}

fun findRegionFrom(type: Char, square: Pair<Int, Int>, currentRegion: MutableSet<Pair<Int, Int>>): Int {
    if (currentRegion.contains(square)) return 0
    if (!isSameRegion(type, square)) return 1
    currentRegion.add(square)

    return findRegionFrom(type, Pair(square.first + 1, square.second), currentRegion) +
           findRegionFrom(type, Pair(square.first - 1, square.second), currentRegion) +
           findRegionFrom(type, Pair(square.first, square.second + 1), currentRegion) +
           findRegionFrom(type, Pair(square.first, square.second - 1), currentRegion)
}

fun isSameRegion(type: Char, square: Pair<Int, Int>): Boolean {
    val (x, y) = square
    return x >= 0 && y >= 0 && y < data.size && x < data[y].size && data[y][x] == type
}
