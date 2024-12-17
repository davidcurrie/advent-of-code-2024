package day17

import java.io.File
import kotlin.math.pow

data class Registers(var a: Long, var b: Long, var c: Long)

fun main() {
    val (first, second) = File("src/main/kotlin/day17/input.txt").readText().split("\n\n")
    val registers = """(\d+)""".toRegex().findAll(first).map { it.groupValues[1].toLong() }.toList().let {
        Registers(it[0], it[1], it[2])
    }
    val program = """(\d+)""".toRegex().findAll(second).map { it.groupValues[1].toLong() }.toList()

    println(run(program, registers).joinToString(","))

    val queue = ArrayDeque(listOf(Pair(1, 0L)))
    while (queue.isNotEmpty()) {
        val (length, start) = queue.removeFirst()
        for (registerA in start until start + 8) {
            if (run(program, registers.copy(a = registerA)) == program.takeLast(length)) {
                queue.add(Pair(length + 1, registerA * 8))
                if (length == program.size) {
                    println(registerA)
                    return
                }
            }
        }
    }
}

private fun run(
    program: List<Long>,
    inputRegisters: Registers,
): List<Long> {
    val registers = inputRegisters.copy()
    val output = mutableListOf<Long>()
    var pointer = 0
    while (pointer in program.indices) {
        val opcode = program[pointer]
        val operand = program[pointer + 1]
        pointer += 2
        when (opcode) {
            0L -> registers.a = (registers.a / 2.0.pow(combo(registers, operand).toDouble())).toLong()
            1L -> registers.b = registers.b.xor(operand)
            2L -> registers.b = combo(registers, operand) and 7
            3L -> if (registers.a != 0L) pointer = operand.toInt()
            4L -> registers.b = registers.b xor registers.c
            5L -> output.add(combo(registers, operand) % 8)
            6L -> registers.b = (registers.a / 2.0.pow(combo(registers, operand).toDouble())).toLong()
            7L -> registers.c = (registers.a / 2.0.pow(combo(registers, operand).toDouble())).toLong()
        }
    }
    return output
}

fun combo(registers: Registers, operand: Long) =
    when (operand) {
        in 0L..3L -> operand
        4L -> registers.a
        5L -> registers.b
        6L -> registers.c
        else -> throw IllegalStateException("Invalid operand $operand")
    }
