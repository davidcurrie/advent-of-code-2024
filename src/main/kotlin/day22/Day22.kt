package day22

import java.io.File

fun main() {
    val input = File("src/main/kotlin/day22/input.txt").readLines()
    println(input.sumOf { (1..2000).fold(it.toInt()) { acc, _ -> evolve(acc) }.toLong() })

    val diffs = input.map { (1..2000).fold(listOf(it.toInt())) { acc, _ -> acc + (evolve(acc.last()))}
        .map { it % 10 }
        .zipWithNext().map { (a, b) -> b to (b - a) }
    }
    val sequenceValues = diffs.map { list ->
        list.windowed(4).map { it.map { it.second } to it.last().first }
            .groupBy { it.first }.map { it.key to it.value.first().second }.toMap()
    }
    val sequences = sequenceValues.flatMap { it.keys }.toSet()
    println(sequences.maxOf { sequence -> sequenceValues.sumOf { it[sequence] ?: 0 } })
}

fun evolve(a: Int): Int {
    val b = prune(mix(a, a * 64))
    val c = prune(mix(b, (b / 32)))
    return prune(mix(c, c * 2048))
}

fun mix(a: Int, b: Int): Int {
    return a.xor(b)
}

fun prune(a: Int): Int {
    return a.mod(16777216)
}