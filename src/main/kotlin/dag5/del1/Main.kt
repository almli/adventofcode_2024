package dag5.del1
import java.io.File

fun main() {
    val lines = File("../data.txt").readLines()
    
    val rules = lines
        .filter { it.contains("|") }
        .map { it.split("|") }
        .map { (a, b) -> a.toInt() to b.toInt() }
    
    val prints = lines
        .filter { it.contains(",") }
        .map { it.split(",") }
        .map { parts -> parts.map { it.toInt() } }
    
    var total = 0
    for (print in prints) {
       var ok = true
       for (rule in rules) {
           if (!followsRule(print, rule)) {
             ok = false
             break
           }  
       }
        if(ok) total += middleNumber(print)
    }

    println(total)
}

fun middleNumber(list: List<Int>): Int {
    return list[(list.size / 2)]
}

fun followsRule(list: List<Int>, rule: Pair<Int, Int>): Boolean {
    var containsFirst = list.contains(rule.first);
    var containsSecond = list.contains(rule.second);
    if (containsFirst != containsSecond) {
        return true
    }
    if (!containsFirst) {
        return true
    }
    var firstIndex = list.indexOf(rule.first)
    var secondIndex = list.indexOf(rule.second)
    return firstIndex < secondIndex
}