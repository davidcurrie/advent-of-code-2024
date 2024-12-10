package day10

import java.io.File

data class Coord(val x: Int, val y: Int) {
    fun neighbours() = setOf(Coord(x - 1, y), Coord(x + 1, y), Coord(x, y - 1), Coord(x, y + 1))
}

fun Map<Coord,  Int>.findTrails(trail: List<Coord>): Set<List<Coord>> {
    if (this[trail.last()] == 9) return setOf(trail)
    return trail.last().neighbours().filter {
        this.contains(it) && !trail.contains(it) && this[it] == this[trail.last()]!! + 1
    }.flatMap { findTrails(trail + it) }.toSet()
}

fun main() {
    val map = File("src/main/kotlin/day10/input.txt").readLines()
        .flatMapIndexed { y, line -> line.mapIndexed { x, c -> Coord(x, y) to c.digitToInt() } }.toMap()
    val trailheads = map.filter { (_, height) -> height == 0 }.keys
    val trailsPerTrailhead = trailheads.map { trailhead -> map.findTrails(listOf(trailhead)) }
    println(trailsPerTrailhead.sumOf { trails -> trails.map { trail -> trail.last() }.toSet().size })
    println(trailsPerTrailhead.sumOf { trails -> trails.size })
}