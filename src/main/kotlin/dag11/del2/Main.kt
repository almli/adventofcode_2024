package dag11.del2
import java.io.File

val MAX_DEPTH = 75
val cache: Array<MutableMap<Long, Long>> = Array(MAX_DEPTH) { mutableMapOf() }

fun main() {
    var stones = File("../data.txt").readText().split("\\s+".toRegex()).map(String::toLong)
    var count =  stones.sumOf {  solveStone(0, it) }
    println(count)
}

fun solveStone(depth: Int, stone:Long): Long {
    if(depth==MAX_DEPTH) return 1
    var cached =  cache[depth].get(stone)
    if (cached != null) {
        return cached
    }
    var cnt:Long? = null;
    if (stone == 0L) {
          cnt =  solveStone(depth+1, 1L)
    } else if (stone.toString().length % 2 == 0) {
        cnt =  solveStone(depth+1,stone.toString().substring(0, stone.toString().length / 2).toLong()) + 
               solveStone(depth+1,  stone.toString().substring(stone.toString().length / 2).toLong())
    } else {
        cnt =  solveStone(depth+1, stone * 2024L)
    }
    cache[depth].put(stone, cnt)
    return cnt
}