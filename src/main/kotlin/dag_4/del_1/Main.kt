package dag_4.del_1
import java.io.File
import kotlin.math.abs

lateinit var data: Array<CharArray>

fun main() {
    val WORD = "XMAS"
    var totalCount = 0
    data = File("../data.txt").readLines().map { it.toCharArray() }.toTypedArray()
    for (i in data.indices) {
        for (j in data[i].indices) {
            totalCount += wordCountFromLocation(i, j, WORD)
        }
    }
    println(totalCount)
}

fun wordCountFromLocationInDirection(i: Int, j: Int, word: String, nextPos: (Int, Int) -> Pair<Int, Int>): Int {
     val (newI, newJ) = nextPos(i, j)
     if(!cellIsPromising(newI,newJ,word)) return 0
    val remainingWord = word.substring(1)
    if (remainingWord.isEmpty()) return 1
    return wordCountFromLocationInDirection(newI, newJ, remainingWord,nextPos)
}

fun wordCountFromLocation(i: Int, j: Int, word: String): Int {
    if(!cellIsPromising(i,j,word)) return 0

    val remainingWord = word.substring(1)
    if (remainingWord.isEmpty()) return 1
    
    return wordCountFromLocationInDirection(i, j, remainingWord, { x, y -> Pair(x - 1 , y) }) +
           wordCountFromLocationInDirection(i, j, remainingWord, { x, y -> Pair(x + 1 , y) }) +
           wordCountFromLocationInDirection(i, j, remainingWord, { x, y -> Pair(x , y - 1) }) +
           wordCountFromLocationInDirection(i, j, remainingWord, { x, y -> Pair(x , y + 1) }) +
           wordCountFromLocationInDirection(i, j, remainingWord, { x, y -> Pair(x - 1 , y - 1) }) +
           wordCountFromLocationInDirection(i, j, remainingWord, { x, y -> Pair(x - 1 , y + 1) }) +
           wordCountFromLocationInDirection(i, j, remainingWord, { x, y -> Pair(x + 1 , y - 1) }) +
           wordCountFromLocationInDirection(i, j, remainingWord, { x, y -> Pair(x + 1 , y + 1) })
}

fun cellIsPromising(i:Int,j:Int, word:String): Boolean {
    return !(i < 0 || j < 0 || i >= data.size || j >= data[i].size) && data[i][j] == word[0]
}

