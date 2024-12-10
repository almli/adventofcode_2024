package dag10.del1
import java.io.File
import kotlin.math.abs

lateinit var data: Array<CharArray>

fun main() {
    val WORD = "0123456789"
    var totalCount = 0
    data = File("../data.txt").readLines().map { it.toCharArray() }.toTypedArray()
    for (i in data.indices) {
        for (j in data[i].indices) {
            totalCount += wordCountFromLocation(i, j, WORD)
        }
    }
    println(totalCount)
}

fun wordCountFromLocation(i: Int, j: Int, word: String): Int {
    if(!cellIsPromising(i,j,word)) return 0

    val remainingWord = word.substring(1)
    if (remainingWord.isEmpty()) {
        return 1
    }
    return wordCountFromLocation(i-1, j, remainingWord) +
        wordCountFromLocation(i+1, j, remainingWord) +
        wordCountFromLocation(i, j-1, remainingWord) +
        wordCountFromLocation(i, j+1, remainingWord) 
}

fun cellIsPromising(i:Int,j:Int, word:String): Boolean {
    return !(i < 0 || j < 0 || i >= data.size || j >= data[i].size) && data[i][j] == word[0]
}

