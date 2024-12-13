package day13

import java.io.File

data class Machine(val a: Coord, val b: Coord, val prize: Coord)
data class Coord(val x: Int, val y: Int)

fun main() {
    val input = File("src/main/kotlin/day13/input.txt").readText().split("\n\n").map {
        it.lines().map { """\d+""".toRegex().findAll(it).map { it.value.toInt() }.toList() }.map { Coord(it[0], it[1]) }
    }.map { Machine(it[0], it[1], it[2]) }
    println(solve(input, 0))
    println(solve(input, 10000000000000))
}

private fun solve(
    input: List<Machine>,
    delta: Long
) = input.sumOf {
    val prizeX = delta + it.prize.x
    val prizeY = delta + it.prize.y
    val b = (it.a.y * prizeX - it.a.x * prizeY).toDouble() / (it.a.y * it.b.x - it.a.x * it.b.y)
    val a = (prizeX - it.b.x * b) / it.a.x
    if (b % 1 == 0.0 && a % 1 == 0.0) (a * 3 + b).toLong() else 0L
}