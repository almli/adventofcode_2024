package dag9.del1
import java.io.File
import kotlin.math.abs

fun main() {
    val data: List<Pair<Int, Int?>> = File("../data.txt").readText().chunked(2).map { it[0].digitToInt() to it.getOrNull(1)?.digitToInt() }
    val tmp: MutableList<Int?> = mutableListOf()
     data.forEachIndexed { index, pair ->
        tmp.addAll(List(pair.first) { index })
        pair.second?.let { tmp.addAll(List(it) { null }) }
    }
    val expandedData: Array<Int?> = tmp.toTypedArray()
    var nextEmtpyIndex = findNextEmpty(expandedData, 0)
    for (i in expandedData.size - 1 downTo 0) {
        if(nextEmtpyIndex>=i) break;   
        if(expandedData[i]!=null) {
            expandedData[nextEmtpyIndex] = expandedData[i];
            expandedData[i] = null;
            nextEmtpyIndex = findNextEmpty(expandedData, nextEmtpyIndex);
            if(nextEmtpyIndex == -1) break;
        }
    }
    val sum = expandedData.withIndex().sumOf { it.index * (it.value?.toLong() ?: 0) }
    //println(expandedData.toList())
    println(sum)
}

fun findNextEmpty(data: Array<Int?>, currentIndex:Int): Int {
    for (i in (currentIndex) until data.size) {
        if(data[i] == null) {
            return i;
        }
    }
    return -1;
}
