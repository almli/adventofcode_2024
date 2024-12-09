package dag9.del2
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

    var i = expandedData.size - 1
    while (i >= 0) {
        while(i >= 0 && expandedData[i] == null) {
            i--
        }
        var currentId = expandedData[i]
        var currentIdEndIndex = i;
        i--
        while(i >= 0 && expandedData[i] == currentId) {
            i--
        }
        if(i<=0) break;
        var currentIdStartIndex = i+1;
        var currentBlockSize = currentIdEndIndex-currentIdStartIndex + 1
        val emptyIndex = findNextEmptyOfSize(expandedData,currentBlockSize , currentIdStartIndex)
        if(emptyIndex >-1) {
            for(j in emptyIndex until (emptyIndex+currentBlockSize)) {
                expandedData[j] = currentId;
                expandedData[currentIdStartIndex+(j-emptyIndex)] = null;
            }
        }
    }

    val sum = expandedData.withIndex().sumOf { it.index * (it.value?.toLong() ?: 0) }
    //println(expandedData.toList().map{it?:'.'}.joinToString(""))
    println(sum)
}

fun findNextEmptyOfSize(data: Array<Int?>, spaceReq:Int, maxIndex:Int): Int {
    for (i in 0 until maxIndex) {
        if(data[i] == null) {
            var emptySize = 0;
            var emptyStartIndex = i
            for(j in i until maxIndex) {
                if(data[j] == null) {
                    emptySize++;
                    if(emptySize==spaceReq) {
                        return emptyStartIndex;
                    }
                } else {
                    break;
                }
            }
        }
    }
    return -1;
}
