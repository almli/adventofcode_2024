package dag4.del_2
import java.io.File
import kotlin.math.abs

lateinit var data: Array<CharArray>

fun main() {
    var totalCount = 0
    data = File("data.txt").readLines().map { it.toCharArray() }.toTypedArray()
    for (i in data.indices) {
        for (j in data[i].indices) {
            totalCount += xOnLocation(i, j)
        }
    }
    println(totalCount)
}

fun xOnLocation(i: Int, j: Int): Int {
    if(!cellIsPromising(i,j,'A')) return 0
    if(
        (cellIsPromising(i-1,j-1,'M') && 
        cellIsPromising(i-1,j+1, 'S') && 
        cellIsPromising(i+1,j-1, 'M') && 
        cellIsPromising(i+1,j+1, 'S')) 
        ||
        (cellIsPromising(i-1,j-1,'S') && 
        cellIsPromising(i-1,j+1, 'M') && 
        cellIsPromising(i+1,j-1, 'S') && 
        cellIsPromising(i+1,j+1, 'M'))
        ||
        (cellIsPromising(i-1,j-1,'M') && 
        cellIsPromising(i-1,j+1, 'M') && 
        cellIsPromising(i+1,j-1, 'S') && 
        cellIsPromising(i+1,j+1, 'S'))
        ||
        (cellIsPromising(i-1,j-1,'S') && 
        cellIsPromising(i-1,j+1, 'S') && 
        cellIsPromising(i+1,j-1, 'M') && 
        cellIsPromising(i+1,j+1, 'M'))
    ) return 1
    return 0

}

fun cellIsPromising(i:Int,j:Int, c:Char): Boolean {
    return !(i < 0 || j < 0 || i >= data.size || j >= data[i].size) && data[i][j] == c
}

