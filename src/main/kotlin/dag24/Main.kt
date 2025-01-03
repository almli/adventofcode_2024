package dag24

import java.io.File
import kotlin.math.abs


data class Gate(val varA: String, val op: String, val varB: String)

fun parseGate(str: String): Gate {
    val regex = """(\w+)\s+(\w+)\s+(\w+)""".toRegex()
    val match = regex.matchEntire(str) ?: throw IllegalArgumentException("Invalid format: $str")
    val (varA, op, varB) = match.destructured
    return Gate(varA, op, varB)
}

fun main() {
    val (initialValues, gates) = File("./data.txt").readLines().let { lines ->
        lines.filter { ":" in it }.map { it.split(":").let { (a, b) -> a.trim() to b.trim().toInt() } } to
                lines.filter { "->" in it }.map { it.split("->").let { (a, b) -> parseGate(a.trim()) to b.trim() } }
    }

    val values = mutableMapOf<String, Boolean>()
    for (v in initialValues) {
        values.put(v.first, v.second == 1)
    }

    while (true) {
        var allEvaluated = true
        for (g in gates) {
            val valA = values[g.first.varA]
            val valB = values[g.first.varB]
            if (valA == null || valB == null) {
                allEvaluated = false
            } else {
                values[g.second] = perform(valA, valB, g.first.op)
            }
        }
        if (allEvaluated) {
            break
        }
    }
    val out = values.filter { it.key.startsWith("z") }.toSortedMap().values.toTypedArray()
    var sum = 0L
    for (i in out.indices) {
        sum += if(out[i]) Math.pow(2.0, i.toDouble()).toLong() else 0
    }
    println(sum)
    println(values.filter { it.key.startsWith("z") }.toSortedMap())
}

fun perform(valA: Boolean, valB: Boolean, op: String): Boolean {
    return when (op) {
        "XOR" -> valA != valB
        "OR" -> valA || valB
        "AND" -> valA && valB
        else -> throw IllegalArgumentException("Unknown operand: $op")
    }
}
