package dag23

import java.io.File
import kotlin.math.abs

fun main() {
    val values = File("./data.txt").readLines()
        .map { it.trim().split("-") }.map { (a, b) -> a to b }

    val networkMap = mutableMapOf<String, MutableSet<String>>()

    for (v in values) {
        networkMap.getOrPut(v.first) { mutableSetOf() }.apply {
            add(v.second)
        }
        networkMap.getOrPut(v.second) { mutableSetOf() }.apply {
            add(v.first)
        }
    }

    println(networkMap)

    val sets = mutableSetOf<List<String>>()

    for (comp in networkMap) {
        for (connectedKey in comp.value) {
            val connected = networkMap[connectedKey]!!
            for (cand in networkMap) {
                if (cand.value.contains(comp.key) && cand.value.contains(connectedKey)) {
                    //  println("comp:${comp.key}, connectedKey:$connectedKey, common:$common")
                    sets.add(listOf(comp.key, connectedKey, cand.key).sorted())
                }
            }

        }
    }
    println(
        sets.sortedBy { it.firstOrNull() }
            .joinToString("\n") { it.joinToString() }
    )
    val count = sets.filter { isMatch(it)}.count()
    println(count)

}

fun isMatch(list: List<String>): Boolean {
    return list.find { it.startsWith("t") }!=null
}
/*
co=[ka, ta, de, tc]

de=[cg, co, ta, ka]


 */