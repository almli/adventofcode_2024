package dag14.del1

import java.io.File


class Robot(
    val xInitPos: Int,
    val yInitPos: Int,
    var xSpeed: Int,
    var ySpeed: Int
) {
    var xPos: Int = xInitPos
    var yPos: Int = yInitPos
    override fun toString(): String = "Robot: ($xPos,$yPos) ($xSpeed,$ySpeed)"
    private val centerX = (SIZE_X / 2);
    private val centerY = (SIZE_Y / 2);
    fun q(): Int = when {
        xPos == centerX || yPos == centerY -> 0
        xPos <= centerX && yPos <= centerY -> 1
        xPos > centerX && yPos <= centerY -> 2
        xPos <= centerX -> 3
        else -> 4
    }
}

val SIZE_X = 101
val SIZE_Y = 103
val SECONDS = 100
fun main() {
    val robots = File("../data.txt").readLines().map { parse(it) }
    for (s in 1..SECONDS) {
        for (robot in robots) {
            robot.xPos = ((robot.xInitPos + robot.xSpeed * s) % SIZE_X).let { if (it < 0) it + SIZE_X else it }
            robot.yPos = ((robot.yInitPos + robot.ySpeed * s) % SIZE_Y).let { if (it < 0) it + SIZE_Y else it }
        }
    }
    val sum = robots.filter { it.q() !=0 }
        .groupingBy { it.q() }
        .eachCount().values.fold(1) { acc, count -> acc * count }
    println(sum)
}

fun parse(line: String): Robot {
    val (xPos, yPos, xSpeed, ySpeed) = """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".toRegex().find(line)!!.destructured
    return Robot(xPos.toInt(), yPos.toInt(), xSpeed.toInt(), ySpeed.toInt())
}
