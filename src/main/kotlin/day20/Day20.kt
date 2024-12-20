package day20

import java.io.File
import java.util.*
import kotlin.math.min

data class Coord(val x: Int, val y: Int) {
    operator fun plus(delta: Coord) = Coord(x + delta.x, y + delta.y)
    operator fun minus(delta: Coord) = Coord(x - delta.x, y - delta.y)
}

val directions = listOf(Coord(1, 0), Coord(0, 1), Coord(-1, 0), Coord(0, -1))

fun main() {
    val grid = File("src/main/kotlin/day20/input.txt").readLines().flatMapIndexed { y, line ->
        line.mapIndexed { x, c -> Coord(x, y) to c }
    }.toMap()
    val walls = grid.filterValues { it == '#' }.keys
    val start = grid.filterValues { it == 'S' }.keys.first()
    val end = grid.filterValues { it == 'E' }.keys.first()
    val track = grid.filterValues { it != '#' }.keys
    val fromStart = paths(track, start)
    val toEnd = paths(track, end)
    val withoutCheat = fromStart[end]!!

    println(solve(track, walls, fromStart, toEnd, withoutCheat, 2))
    println(solve(track, walls, fromStart, toEnd, withoutCheat, 20))
}

private fun solve(
    track: Set<Coord>,
    walls: Set<Coord>,
    fromStart: Map<Coord, Int>,
    toEnd: Map<Coord, Int>,
    withoutCheat: Int,
    maxCheat: Int
) = track.flatMap { cheatStart ->
        cheats(cheatStart, walls, track, maxCheat)
            .map { (cheatEnd, length) -> fromStart[cheatStart]!! + length + toEnd[cheatEnd]!! }
    }.count { withoutCheat - it >= 100 }


private fun paths(track: Set<Coord>, start: Coord): Map<Coord, Int> {
    val queue: Queue<Coord> = LinkedList()
    val visited = mutableSetOf(start)
    val distances = mutableMapOf(start to 0)

    queue.add(start)
    while (queue.isNotEmpty()) {
        val current = queue.poll()
        directions.map { current + it }.filter { it in track && it !in visited }
            .forEach {
                visited.add(it)
                distances[it] = distances[current]!! + 1
                queue.add(it)
            }
    }

    return distances
}

private fun cheats(start: Coord, walls: Set<Coord>, track: Set<Coord>, max: Int): Map<Coord, Int> {
    val queue = ArrayDeque(listOf(start to 0))
    val visited = mutableSetOf(start)
    val cheats = mutableMapOf<Coord, Int>()
    while (queue.isNotEmpty()) {
        val (current, distance) = queue.removeFirst()
        if (current != start && current in track) {
            cheats[current] = if (current in cheats) min(cheats[current]!!, distance) else distance
        }
        if (distance < max) {
            directions.map { current + it }
                .filter { (it in walls || it in track) && it !in visited }
                .forEach {
                    visited.add(it)
                    queue.add(it to distance + 1)
                }
        }
    }
    return cheats
}