package day23

import java.io.File

fun main() {
    val input = File("src/main/kotlin/day23/input.txt")
        .readLines().map { it.split("-" ) }
    val sets = input.map { it.toSet() }.toSet()
    val connections = input
        .flatMap { (a, b) -> setOf(a to b, b to a) }
        .groupBy { it.first }.map { it.key to it.value.map { it.second }.toSet() }
        .toMap()

    println(grow(sets, connections).count { it.any { it.startsWith("t") } })
    println(generateSequence(sets) { grow(it, connections) }.first { it.size == 1 }
        .first().sorted().joinToString(","))
}

private fun grow(sets: Set<Set<String>>, connections: Map<String, Set<String>>) =
    sets.flatMap { set -> set.map { connections[it]!! }.reduce(Set<String>::intersect).map { set + it } }.toSet()
