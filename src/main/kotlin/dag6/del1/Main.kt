package dag6.del1
import java.io.File
import kotlin.math.abs

lateinit var data: Array<CharArray>

class Navigator(private var pos: Pair<Int, Int>) {
    private var direction = Direction.UP

    private fun findNextPos(): Pair<Int, Int> {
        return when (direction) {
            Direction.UP -> Pair(pos.first - 1, pos.second)
            Direction.RIGHT -> Pair(pos.first, pos.second + 1)
            Direction.DOWN -> Pair(pos.first + 1, pos.second)
            Direction.LEFT -> Pair(pos.first, pos.second - 1)
        }
    }
    fun nextVal(): Char? {
        val nextPos = findNextPos()
        return if (nextPos.first < 0 || nextPos.second < 0 || nextPos.first >= data.size || nextPos.second >= data[nextPos.first].size) null else data[nextPos.first][nextPos.second]
    }
   fun move():Boolean {
        var newVistited = false
        if( data[pos.first][pos.second] != 'X') {
            data[pos.first][pos.second] = 'X'
            newVistited =  true
        }
        pos = findNextPos()
        return newVistited
    }
    fun turnRight() {
        direction = Direction.values()[(direction.ordinal + 1) % Direction.values().size]
    }
    enum class Direction {
        UP, RIGHT, DOWN, LEFT
    }
    fun position() = pos
}
fun main() {
    var totalCount = 1
    data = File("../data.txt").readLines().map { it.toCharArray() }.toTypedArray()
    var nav = Navigator(findPos())
    while(true) {
        var nextVal =  nav.nextVal()
        if(nextVal  == null) break
        if(nextVal == '#') nav.turnRight()
        else {
            if(nav.move())
                totalCount++
        }
    }
    println(totalCount)
}
fun findPos(): Pair<Int, Int> {
    for (i in data.indices) {
        for (j in data[i].indices) {
            if(data[i][j] == '^') return Pair(i,j)
        }
    }
    throw Exception("No pos found")
}

