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
            val borders = findRegionFrom(data[i][j], Pair(j, i), currentRegion, 'R')
            found.addAll(currentRegion)
            sum += (borderCount(borders) * currentRegion.size)
        }
    }
    println(sum)
}

fun borderCount(borders: List<Pair<Char, Pair<Int, Int>>>): Int {
    var count = 0
    for (dirBorders in borders.groupBy({ it.first }, { it.second })) {
        if (dirBorders.key == 'R' || dirBorders.key == 'L') {
            count += countVerticalBorders(dirBorders.value)
        } else {
            count += countHorizontalBorders(dirBorders.value)
        }
    }
    return count;
}

fun countHorizontalBorders(borders: List<Pair<Int, Int>>): Int {
    return borders
        .groupBy({ it.second }, { it.first })
        .values
        .sumOf { segmentCount(it) }
}

fun countVerticalBorders(borders: List<Pair<Int, Int>>): Int {
    return borders
        .groupBy({ it.first }, { it.second })
        .values
        .sumOf { segmentCount(it) }
}

fun segmentCount(ints: List<Int>): Int {
    if (ints.isEmpty()) return 0

    return ints
        .sorted()
        .zipWithNext()
        .count { it.second != it.first + 1 } + 1
}

fun findRegionFrom(
    type: Char,
    square: Pair<Int, Int>,
    currentRegion: MutableSet<Pair<Int, Int>>,
    dir: Char
): List<Pair<Char, Pair<Int, Int>>> {
    if (currentRegion.contains(square)) return emptyList()
    if (!isSameRegion(type, square)) return listOf(Pair(dir, square))
    currentRegion.add(square)

    return findRegionFrom(type, Pair(square.first + 1, square.second), currentRegion, 'R') +
            findRegionFrom(type, Pair(square.first - 1, square.second), currentRegion, 'L') +
            findRegionFrom(type, Pair(square.first, square.second + 1), currentRegion, 'D') +
            findRegionFrom(type, Pair(square.first, square.second - 1), currentRegion, 'U')
}

fun isSameRegion(type: Char, square: Pair<Int, Int>): Boolean {
    val (x, y) = square
    return x >= 0 && y >= 0 && y < data.size && x < data[y].size && data[y][x] == type
}
