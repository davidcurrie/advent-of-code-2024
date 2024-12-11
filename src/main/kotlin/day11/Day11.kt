package day11

import java.io.File

fun main() {
    val stones = File("src/main/kotlin/day11/input.txt").readText().split(" ").map { it.toLong() }
    val memo = mutableMapOf<Pair<Int, Long>, Long>()
    println(stones.sumOf { split(25, it, memo) })
    println(stones.sumOf { split(75, it, memo) })
}

private fun split(iterations: Int, stone: Long, memo: MutableMap<Pair<Int, Long>, Long>): Long {
    if (iterations == 0) {
        return 1
    }
    val memoKey = Pair(iterations - 1, stone)
    return if (memoKey in memo) {
        memo[memoKey]!!
    } else {
        if (stone == 0L) {
            listOf(1L)
        } else {
            val stringStone = stone.toString()
            if (stringStone.length % 2 == 0) {
                listOf(stringStone.substring(0, stringStone.length / 2).toLong(),
                    stringStone.substring(stringStone.length / 2).toLong())
            } else {
                listOf(stone * 2024)
            }
        }.sumOf { newStone -> split(iterations - 1, newStone, memo) }.also { memo[memoKey] = it }
    }
}