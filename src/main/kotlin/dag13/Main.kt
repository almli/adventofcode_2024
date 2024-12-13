package dag13
import java.io.File

class Machine(
    val ax: Long,
    val ay: Long,
    val bx: Long,
    val by: Long,
    val px: Long,
    val py: Long
)

fun main() {
    println(calc(0))
    println(calc(10000000000000))
}

private fun calc(extra: Long): Long {
    val machines = File("data.txt").readText().trim().split("\n\n").map { it.lines() }.map { parse(it, extra) };
    val sum = machines.sumOf {
        //disse fomlene er funnet ved å løse ligningene med to ukjente  px=b*bxstep+a*axstep , py=b*bystep+a*aystep
        val t = it.px * it.ay - it.py * it.ax
        val n = it.bx * it.ay - it.by * it.ax
        if (t % n != 0L) return@sumOf 0L
        val b = t / n
        val a = (it.px - b * it.bx) / it.ax
        a * 3L + b
    }
    return sum
}

fun parse(lines: List<String>, extra: Long): Machine {
    val regXY = """X\+(\d+), Y\+(\d+)""".toRegex()
    val regP = """X\=(\d+), Y\=(\d+)""".toRegex()
    val (ax, ay) = regXY.find(lines.get(0))!!.destructured
    val (bx, by) = regXY.find(lines.get(1))!!.destructured
    val (px, py) = regP.find(lines.get(2))!!.destructured
    return Machine(ax.toLong(), ay.toLong(), bx.toLong(), by.toLong(), px.toLong() + extra, py.toLong() + extra)
}