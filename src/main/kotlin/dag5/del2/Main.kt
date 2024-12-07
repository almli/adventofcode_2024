package dag5.del_2
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
       var fixedPrint: List<Int>? = null 
       for (rule in rules) {
           if (!followsRule(print, rule)) {
             fixedPrint = fix(print, rules)
             break
           }  
       }
        if(fixedPrint!=null) total += middleNumber(fixedPrint)
    }
    println(total)
}

fun fix(print: List<Int> , rules: List<Pair<Int, Int>>): List<Int> {
    val fixedPrint = print.map { it.toString().toInt() }.toMutableList()
    
    while (true) {
        var changed = false
        for (rule in rules) {
            if (!followsRule(fixedPrint, rule)) {
                val firstIndex = fixedPrint.indexOf(rule.first)
                val secondIndex = fixedPrint.indexOf(rule.second)
                
                if (firstIndex > secondIndex) {
                    val element = fixedPrint.removeAt(firstIndex)
                    fixedPrint.add(secondIndex, element)
                    changed = true
                }
            }
        }
        if (!changed) break
    }
    
    return fixedPrint
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