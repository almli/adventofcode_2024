package dag1
import java.io.File
import kotlin.math.abs

fun main() {
    val (col1, col2) = File("data.txt").readLines()
        .map { it.trim().split("\\s+".toRegex()) }
        .map { (a, b) -> a.toInt() to b.toInt() }
        .unzip()

    val sortedCol1 = col1.sorted()
    val sortedCol2 = col2.sorted()
    val totalDiff = (sortedCol1 zip sortedCol2).sumOf { (a, b) -> abs(a - b) }
    println(totalDiff)

    val freq2 = sortedCol2.groupingBy { it }.eachCount()
    val result = sortedCol1.sumOf { freq2.getOrDefault(it, 0) * it }
    println(result)
}
