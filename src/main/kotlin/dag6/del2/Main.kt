package dag6.del2
import java.io.File
import kotlin.math.abs

lateinit var data: Array<CharArray>
lateinit var startPos: Pair<Int, Int>
class Navigator(private var pos: Pair<Int, Int>) {
    private var direction = Direction.U

    private fun findNextPos(): Pair<Int, Int> {
        return when (direction) {
            Direction.U -> Pair(pos.first - 1, pos.second)
            Direction.R -> Pair(pos.first, pos.second + 1)
            Direction.D -> Pair(pos.first + 1, pos.second)
            Direction.L -> Pair(pos.first, pos.second - 1)
        }
    }
  
    fun nextVal(): Char? {
        val nextPos = findNextPos()
        return if (nextPos.first < 0 || nextPos.second < 0 || nextPos.first >= data.size || nextPos.second >= data[nextPos.first].size) null else data[nextPos.first][nextPos.second]
    }

    fun move():Boolean {
        var newVistited = false
        pos = findNextPos()
        if( data[pos.first][pos.second] != 'X') {
            data[pos.first][pos.second] = 'X'
            newVistited =  true
        }
       
        return newVistited
    }

    fun moveCheckLoop():Boolean {
        var loop = false
        if( data[pos.first][pos.second] == '.') {
            data[pos.first][pos.second] = direction.name[0]
        } else if(data[pos.first][pos.second]==direction.name[0]){
            loop = true
        }
        pos = findNextPos()
        return loop
    }

    fun turnRight() {
        direction = Direction.values()[(direction.ordinal + 1) % Direction.values().size]
    }

    enum class Direction {
        U, R, D, L
    }

    fun position() = pos
}

fun main() {
    var totalCount = 0
    data = File("../data.txt").readLines().map { it.toCharArray() }.toTypedArray()
    startPos = findStartPos()
    var path = findPath()
    for(pos in path) {
        data = File("../data.txt").readLines().map { it.toCharArray() }.toTypedArray()
        data[pos.first][pos.second]='#'
        if(isLoop()) {
            totalCount++
        }
    }
    println(totalCount)
}

fun findPath():List<Pair<Int,Int>> {
    var path = mutableListOf<Pair<Int,Int>>()
    var nav = Navigator(startPos)
    while(true) {
        var nextVal =  nav.nextVal()
        if(nextVal  == null) break
        if(nextVal == '#') 
            nav.turnRight()
        else {
            if(nav.move())
                path.add(nav.position())
        }
    }
    return path
}

fun isLoop(): Boolean {
    var nav = Navigator(startPos)
    while(true) {
        var nextVal =  nav.nextVal()
        if(nextVal  == null) return false
        if(nextVal == '#') nav.turnRight()
        else {
            if(nav.moveCheckLoop())
                return true
        }
    }
}

fun findStartPos(): Pair<Int, Int> {
    for (i in data.indices) {
        for (j in data[i].indices) {
            if(data[i][j] == '^') return Pair(i,j)
        }
    }
    throw Exception("No pos found")
}

