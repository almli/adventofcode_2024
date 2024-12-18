package dag17.del1

import java.io.File
import kotlin.math.pow


var reg_a: Int = 0
var reg_b: Int = 0
var reg_c: Int = 0

var pointer = 0
val out = mutableListOf<Int>()

fun main() {
    val data = File("../data.txt").readLines();
    reg_a = parseReg(data.get(0))
    reg_b = parseReg(data.get(1))
    reg_c = parseReg(data.get(2))
    val program = data.get(4).substring(8).split(",").map { it.trim().toInt() }.toTypedArray()
    while (true) {
        if (pointer >= program.size - 1) break
        val instr = program[pointer]
        val op = program[pointer + 1]
        pointer = executeInstruction(instr, op)
    }
    println("a: $reg_a, b: $reg_b, c: $reg_c , program: ${program.map { it.toString() }.joinToString(",")}")
    println("out: " + out.map { it.toString() }.joinToString(","))
}

fun getOpValue(op: Int): Int {
    return when (op) {
        in 0..3 -> op
        4 -> reg_a
        5 -> reg_b
        6 -> reg_c
        else -> throw IllegalArgumentException("Unknown operand: $op")
    }
}


fun executeInstruction(instruction: Int, operand: Int): Int {
    return when (instruction) {
        0 -> {
            reg_a = (reg_a.toDouble() / 2.toDouble().pow(getOpValue(operand))).toInt()
            return pointer + 2
        }

        1 -> {
            reg_b = reg_b xor operand
            return pointer + 2
        }

        2 -> {
            reg_b = getOpValue(operand) % 8
            return pointer + 2
        }

        3 -> {
            if (reg_a == 0)
                return pointer + 2
            return operand
        }

        4 -> {
            reg_b = reg_b xor reg_c
            return pointer + 2
        }

        5 -> {
            out.add(getOpValue(operand) % 8)
            return pointer + 2
        }

        6 -> {
            reg_b = (reg_a.toDouble() / 2.toDouble().pow(getOpValue(operand))).toInt()
            return pointer + 2
        }

        7 -> {
            reg_c = (reg_a.toDouble() / 2.toDouble().pow(getOpValue(operand))).toInt()
            return pointer + 2
        }

        else -> throw IllegalArgumentException("Unknown instruction: $instruction")
    }
}

fun parseReg(line: String): Int {
    val regex = """:\s*(\d+)""".toRegex()
    return regex.find(line)?.groupValues?.get(1)?.toInt() ?: 0
}
