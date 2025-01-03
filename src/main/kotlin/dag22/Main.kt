package dag1

import java.io.File
import kotlin.math.abs

fun main() {
    val values = File("data.txt").readLines()
        .map { it.trim().toULong() }.toTypedArray()

    println(values.map { it.toString() })
    var sum = 0UL
    for(v in values) {
        sum += next(v, 2000)
    }
    println(sum)
}
fun next(current: ULong, gen: Int): ULong {
    var next = current
    repeat(gen) {
        next = next(next)
    }
    return next
}
fun next(current: ULong): ULong {
    var newSecret = prune(mix(64UL * current, current))
    newSecret = prune( mix(newSecret/32UL, newSecret))
    return  prune(mix(newSecret* 2048UL, newSecret))
}

fun prune(newBase: ULong): ULong {
    return newBase % 16777216UL
}

fun mix(newBase: ULong, current: ULong): ULong {
    return newBase xor current
}

