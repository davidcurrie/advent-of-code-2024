package day6

import java.io.File

data class Coord(val x: Int, val y: Int)
data class Delta(val x: Int, val y: Int)
data class Guard(val location: Coord, val directionIndex: Int) {
    fun move() = Guard(
        Coord(location.x + directions[directionIndex].x, location.y + directions[directionIndex].y),
        directionIndex
    )
    fun rotate() = Guard(location, (directionIndex + 1) % directions.size)
}
val directions = listOf(Delta(0, -1), Delta(1, 0), Delta(0, 1), Delta(-1, 0))

fun countVisited(grid: Map<Coord, Char>, guard: Guard, visited: Set<Coord>): Int {
    if (!grid.containsKey(guard.location)) return visited.size
    val potentialNextGuard = guard.move()
    val nextGuard = if (grid[potentialNextGuard.location] == '#') guard.rotate() else potentialNextGuard
    return countVisited(grid, nextGuard, visited + guard.location)
}

private fun isLoop(
    start: Guard,
    grid: Map<Coord, Char>,
    obstruction: Coord
): Boolean {
    var guard = start
    val previous = mutableSetOf<Guard>()
    var isLoop = false
    while (true) {
        if (previous.contains(guard)) {
            isLoop = true
            break
        }
        previous.add(guard)
        val potentialNextGuard = guard.move()
        if (grid.containsKey(potentialNextGuard.location)) {
            guard = if (grid[potentialNextGuard.location] == '#' || potentialNextGuard.location == obstruction) {
                guard.rotate()
            } else {
                potentialNextGuard
            }
        } else {
            break
        }
    }
    return isLoop
}

fun main() {
    val grid = File("src/main/kotlin/day6/input.txt").readLines()
        .flatMapIndexed { y, line -> line.mapIndexed { x, c -> Coord(x, y) to c } }.toMap()
    val start = Guard(grid.filter { (_, char) -> char == '^' }.keys.single(), 0)

    println(countVisited(grid, start, emptySet()))
    println(grid.filter { (_, char) -> char == '.' }.keys.count { isLoop(start, grid, it) })
}