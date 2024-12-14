package dag14.del2

import java.io.File

class Robot(
    val xInitPos: Long,
    val yInitPos: Long,
    var xSpeed: Long,
    var ySpeed: Long
) {
    var xPos: Long = xInitPos
    var yPos: Long = yInitPos
    override fun toString(): String = "Robot: ($xPos,$yPos) ($xSpeed,$ySpeed)"
    val centerX = (SIZE_X / 2);
    val centerY = (SIZE_X / 2);
    fun q(): Int {
        val xOutsideCount = centerX / 3
        val yOutsideCount = centerY / 3
        return if (xPos + 1 >= xOutsideCount && xPos  < (SIZE_X - xOutsideCount) &&
            yPos   >= yOutsideCount && yPos  < (SIZE_Y - yOutsideCount)) 1 else 0
    }
}

val SIZE_X = 101
val SIZE_Y = 103
val SECONDS = 10000000

fun main() {
    val robots = File("../data.txt").readLines().map { parse(it) }
    val robotCount = robots.size;
    // printPattern(robots)
    var limitCount =  robotCount*0.8
    for (s in 1..SECONDS) {
        for (robot in robots) {
            robot.xPos = ((robot.xInitPos + robot.xSpeed * s) % SIZE_X).let { if (it < 0) it + SIZE_X else it }
            robot.yPos = ((robot.yInitPos + robot.ySpeed * s) % SIZE_Y).let { if (it < 0) it + SIZE_Y else it }

        }
        val count = robots.filter { it.q() == 1 }.count()
        if (count >= limitCount) {
            println(s)
            printPattern(robots)
            break
        }
    }
}

fun printPattern(robots: List<Robot>) {
    val grouped = robots.groupBy { it.yPos }.mapValues { it.value.groupBy { robot -> robot.xPos } }
    for (y in 0L..<SIZE_Y) {
        for (x in 0L..<SIZE_X) {
            if (grouped[y]?.containsKey(x) == true) {
                print("X")
            } else {
                print(".")
            }
        }
        println()
    }
}

fun parse(line: String): Robot {
    val (xPos, yPos, xSpeed, ySpeed) = """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".toRegex().find(line)!!.destructured
    return Robot(xPos.toLong(), yPos.toLong(), xSpeed.toLong(), ySpeed.toLong())
}
