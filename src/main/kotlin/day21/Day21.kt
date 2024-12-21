package day21

import java.io.File

typealias Keypad = Map<Char, Coord>

data class Coord(val x: Int, val y: Int) {
    operator fun plus(delta: Coord) = Coord(x + delta.x, y + delta.y)
}

val numericKeypad = mapOf(
    '7' to Coord(0, 0), '8' to Coord(1, 0), '9' to Coord(2, 0),
    '4' to Coord(0, 1), '5' to Coord(1, 1), '6' to Coord(2, 1),
    '1' to Coord(0, 2), '2' to Coord(1, 2), '3' to Coord(2, 2),
    '0' to Coord(1, 3), 'A' to Coord(2, 3)
)

val robotKeypad = mapOf(
    '^' to Coord(1, 0), 'A' to Coord(2, 0),
    '<' to Coord(0, 1), 'v' to Coord(1, 1), '>' to Coord(2, 1)
)

fun main() {
    val input = File("src/main/kotlin/day21/input.txt").readLines()

    println(solve(2, input))
    println(solve(25, input))
}

private fun solve(robots: Int, input: List<String>): Long {
    val keypads = List(robots + 1) { if (it < robots) robotKeypad else numericKeypad }
    val lengths = keypads.fold(emptyMap<Pair<Char, Char>, Long>()) { acc, keypad -> shortestPaths(acc, keypad) }
    return input.sumOf { line ->
        line.take(3).toInt() * "A$line".zipWithNext().sumOf { lengths[Pair(it.first, it.second)]!! }
    }
}

private fun shortestPaths(lengths: Map<Pair<Char, Char>, Long>, keypad: Keypad) =
    keypad.flatMap { (startKey, startCoord) ->
        keypad.map { (endKey, endCoord) ->
            Pair(startKey, endKey) to paths(startCoord, endCoord, keypad)
                .minOf { route -> "A$route".zipWithNext().sumOf { lengths[it] ?: 1 } }
        }
    }.toMap()

private fun paths(start: Coord, end: Coord, keypad: Keypad): Set<String> {
    if (start == end) return setOf("A")
    val results = mutableSetOf<String>()
    if (end.x > start.x && (start + Coord(1, 0)) in keypad.values) {
        results.addAll(paths(start + Coord(1, 0), end, keypad).map { ">$it" })
    }
    if (end.x < start.x && (start + Coord(-1, 0)) in keypad.values) {
        results.addAll(paths(start + Coord(-1, 0), end, keypad).map { "<$it" })
    }
    if (end.y > start.y && (start + Coord(0, 1)) in keypad.values) {
        results.addAll(paths(start + Coord(0, 1), end, keypad).map { "v$it" })
    }
    if (end.y < start.y && (start + Coord(0, -1)) in keypad.values) {
        results.addAll(paths(start + Coord(0, -1), end, keypad).map { "^$it" })
    }
    return results
}
