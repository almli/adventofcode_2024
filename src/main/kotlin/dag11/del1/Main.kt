package dag11.del1
import java.io.File
 
val MAX_DEPTH = 25
fun main() {
    var stones = File("../data.txt").readText().split("\\s+".toRegex()).map(String::toLong)
    var count =  stones.sumOf {  solveStone(0, it) }
    println(count)
}
fun solveStone(depth: Int, stone:Long): Long {
    if(depth==MAX_DEPTH) return 1
    if (stone == 0L) {
        return solveStone(depth+1, 1L)
    } else if (stone.toString().length % 2 == 0) {
        return solveStone(depth+1,stone.toString().substring(0, stone.toString().length / 2).toLong()) + 
               solveStone(depth+1,  stone.toString().substring(stone.toString().length / 2).toLong())
    } else {
        return solveStone(depth+1, stone * 2024L)
    }
}
//218956
