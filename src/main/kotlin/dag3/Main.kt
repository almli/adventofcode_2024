package dag3
import java.io.File
import kotlin.math.abs

fun main() {
    val text = File("data.txt").readText()
    val pattern = Regex("""mul\(\d{1,3},\d{1,3}\)|do\(\)|don't\(\)""")
    val matches = pattern.findAll(text).map { it.value }.toList()

 
    var includeMode = true
    val clean = mutableListOf<String>()

    for (m in matches) {
        if (includeMode && m.startsWith("mul")) {
            clean.add(m)
        }
        if (m == "don't()") {
            includeMode = false
        } else if (m == "do()") {
            includeMode = true
        }
    }

    val pattern2 = Regex("""mul\((\d{1,3}),(\d{1,3})\)""") 
    val total = clean.sumOf {
        val (a, b) = pattern2.find(it)!!.destructured
        a.toInt() * b.toInt()
    } 

    println(total)
}