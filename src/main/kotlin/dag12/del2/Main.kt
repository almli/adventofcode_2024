package dag12.del2
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
            val borders = findRegionFrom(data[i][j], Pair(j, i), currentRegion,"X")
            println(data[i][j] + ":H:"+ splitByHolesInSequence(borders, "H") + ":V:"+ splitByHolesInSequence(borders, "V"))
            found.addAll(currentRegion)
            sum += (splitByHolesInSequence(borders, "H").size + splitByHolesInSequence(borders, "V").size) * currentRegion.size
        }
    }
    println(sum)
}

fun findRegionFrom(type: Char,  square: Pair<Int, Int>, currentRegion: MutableSet<Pair<Int, Int>>, dir: String ): List<Pair<String, Pair<Int, Int>>> {
    if (currentRegion.contains(square)) return emptyList()
    if (!isSameRegion(type, square)) return listOf(Pair(dir, square))
    currentRegion.add(square)

    return findRegionFrom(type, Pair(square.first + 1, square.second), currentRegion, "H") +
           findRegionFrom(type, Pair(square.first - 1, square.second), currentRegion, "H") +
           findRegionFrom(type, Pair(square.first, square.second + 1), currentRegion, "V") +
           findRegionFrom(type, Pair(square.first, square.second - 1), currentRegion, "V")
}

fun isSameRegion(type: Char, square: Pair<Int, Int>): Boolean {
    val (x, y) = square
    return x >= 0 && y >= 0 && y < data.size && x < data[y].size && data[y][x] == type
}
fun splitByHolesInSequence(borders: List<Pair<String, Pair<Int, Int>>>, dir: String): Map<Int, List<List<Int>>> {
    return borders
        .filter { it.first == dir }
        .groupBy { if (dir == "H") it.second.first else it.second.second } // Group by second for "H", first for "V"
        .mapValues { (_, group) ->
            group.map { if (dir == "H") it.second.second else it.second.first }
                .distinct()
                .sorted()
                .splitByHoles()
        }
}

private fun List<Int>.splitByHoles(): List<List<Int>> {
    if (isEmpty()) return emptyList()

    val result = mutableListOf<MutableList<Int>>()
    var currentGroup = mutableListOf<Int>()
    currentGroup.add(this.first())

    for (i in 1 until this.size) {
        if (this[i] == this[i - 1] + 1) {
            currentGroup.add(this[i])
        } else {
            result.add(currentGroup)
            currentGroup = mutableListOf(this[i])
        }
    }
    result.add(currentGroup)

    return result
}

