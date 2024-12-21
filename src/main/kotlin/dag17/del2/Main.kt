package dag17.del2

import java.io.File
import kotlin.math.pow


var init_reg_a: ULong = 0UL
var init_reg_b: ULong = 0UL
var init_reg_c: ULong = 0UL

var reg_a: ULong = 0UL
var reg_b: ULong = 0UL
var reg_c: ULong = 0UL
var pointer = 0
var out = mutableListOf<UInt>()
var progSize = 0
var progStr = ""

fun main() {
    val data = File("../data.txt").readLines();
    init_reg_a = parseReg(data.get(0))
    init_reg_b = parseReg(data.get(1))
    init_reg_c = parseReg(data.get(2))
    val program = data.get(4).substring(8).split(",").map { it.trim().toUInt() }.toTypedArray()
    progSize = program.size;
    progStr = program.map { it.toString() }.joinToString(",")
    var i = 10000000000000U
    while (true) {
        if (tryWithValue(i, program)) {
            println("Found value: $i")
            break
        }
        if (out.size > 9)
            println("Tried value: $i, out size: ${out.size}")
        i = i + 1U
    }

    while (true) {
        if (pointer >= program.size - 1) break
        val instr = program[pointer]
        val op = program[pointer + 1]
        pointer = executeInstruction(instr, op)
    }
    println("a: $reg_a, b: $reg_b, c: $reg_c , program: ${program.map { it.toString() }.joinToString(",")}")
    println("out: " + out.map { it.toString() }.joinToString(","))
}


fun tryWithValue(regVal: ULong, program: Array<UInt>): Boolean {
    initProg()
    reg_a = regVal
    val visited = mutableSetOf<String>()
    var i = 0
    val pointerLimit = program.size - 1
    while (true) {
        try {
            if (pointer >= pointerLimit) break
            val instr = program[pointer.toInt()]
            val op = program[pointer.toInt() + 1]
            pointer = executeInstruction(instr, op)
            if (out.size == 1 && out[0] != program[0]) return false
            if (out.size == 2 && out[1] != program[1]) return false
            if (out.size == 3 && out[2] != program[2]) return false
            if (out.size == 4 && out[3] != program[3]) return false
            if (out.size == 5 && out[4] != program[4]) return false
            if (out.size == 6 && out[5] != program[5]) return false
            if (out.size == 7 && out[6] != program[6]) return false
            if (out.size == 8 && out[7] != program[7]) return false
            if (out.size == 9 && out[8] != program[8]) return false
            if (out.size == 10 && out[9] != program[9]) return false
            if (out.size == 11 && out[10] != program[10]) return false

            if (out.size > progSize) return false

            if (i > 100) {
                val state = "$pointer,$reg_a,$reg_b,$reg_c"
                if (visited.contains(state)) return false
                visited.add(state)
            }
        } catch (e: java.lang.IllegalArgumentException) {
            println(e.toString())
            return false
        }
        i++
    }
    return out.size == progSize && out.joinToString(",") == progStr
}


fun initProg() {
    out = mutableListOf<UInt>()
    reg_a = init_reg_a
    reg_b = init_reg_b
    reg_c = init_reg_c
    pointer = 0
}

fun getOpValue(op: UInt): ULong {
    return when (op) {
        in 0U..3U -> op.toULong()
        4U -> reg_a
        5U -> reg_b
        6U -> reg_c
        else -> throw IllegalArgumentException("Unknown operand: $op")
    }
}


fun executeInstruction(instruction: UInt, operand: UInt): Int {
    return when (instruction) {
        0U -> {
            reg_a = (reg_a.toDouble() / 2.toDouble().pow(getOpValue(operand).toDouble())).toULong()
            return pointer + 2
        }

        1U -> {
            reg_b = reg_b xor operand.toULong()
            return pointer + 2
        }

        2U -> {
            reg_b = getOpValue(operand).toULong() % 8UL
            return pointer + 2
        }

        3U -> {
            if (reg_a == 0UL)
                return pointer + 2
            return operand.toInt()
        }

        4U -> {
            reg_b = reg_b xor reg_c
            return pointer + 2
        }

        5U -> {
            out.add((getOpValue(operand) % 8UL).toUInt())
            return pointer + 2
        }

        6U -> {
            reg_b = (reg_a.toDouble() / 2.toDouble().pow(getOpValue(operand).toDouble())).toULong()
            return pointer + 2
        }

        7U -> {
            reg_c = (reg_a.toDouble() / 2.toDouble().pow(getOpValue(operand).toDouble())).toULong()
            return pointer + 2
        }

        else -> throw IllegalArgumentException("Unknown instruction: $instruction")
    }
}

fun parseReg(line: String): ULong {
    val regex = """:\s*(\d+)""".toRegex()
    return regex.find(line)?.groupValues?.get(1)?.toULong() ?: 0UL
}
