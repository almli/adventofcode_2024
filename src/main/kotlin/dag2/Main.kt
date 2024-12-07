package dag2
import java.io.File
import kotlin.math.abs

fun main() {
    val data = File("data.txt").readLines().map { it.split("\\s+".toRegex()).map(String::toInt) }

    var sum = 0
    data.forEach { line ->
        if(safe(line))
          sum++
        else  if(line.indices.any { i -> safe(line.filterIndexed { idx, _ -> idx != i }) })
           sum++
    }
    
    println(sum)
}
fun safe(line: List<Int>): Boolean {
    if (line.size < 2) return false
    if (line != line.sorted() && line != line.sortedDescending()) return false
    val diffs = line.zipWithNext { a, b -> abs(b - a) }
    val maxDiff = diffs.maxOrNull() ?: return false
    val minDiff = diffs.minOrNull() ?: return false
    return maxDiff <= 3 && minDiff >= 1
}