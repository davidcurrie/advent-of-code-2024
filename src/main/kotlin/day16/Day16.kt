package day16
import java.io.File
import java.util.PriorityQueue

val directions = listOf(Coord(1, 0), Coord(0, 1), Coord(-1, 0), Coord(0, -1))

data class Coord(val x: Int, val y: Int) {
    operator fun plus(delta: Coord) = Coord(x + delta.x, y + delta.y)
}

data class State(val path: List<Coord>, val cost: Int, val direction: Int)

fun main() {
    val grid = File("src/main/kotlin/day16/input.txt").readLines().mapIndexed { y, line ->
        line.mapIndexed { x, c -> Coord(x, y) to c }
    }.flatten().toMap()

    val start = grid.filterValues { it == 'S' }.keys.first()
    val end = grid.filterValues { it == 'E' }.keys.first()
    val walls = grid.filterValues { it == '#' }.keys.map { Coord(it.x, it.y) }.toSet()

    val queue = PriorityQueue<State>(compareBy { it.cost })
    queue.add(State(listOf(start), 0, 0))

    val visited = mutableMapOf<Pair<Coord, Int>, Int>()
    val solutions = mutableSetOf<State>()
    while (queue.isNotEmpty() && (solutions.isEmpty() || queue.peek().cost <= solutions.first().cost)) {
        val (path, currentCost, currentDirection) = queue.remove()
        val visit = path.last() to currentDirection
        if (visited[visit]?.let { it < currentCost } == true) continue
        visited[visit] = currentCost
        if (path.last() == end) solutions.add(State(path, currentCost, currentDirection))

        directions.forEachIndexed { index, direction ->
            val next = path.last() + direction
            if (next !in walls) {
                val turnCost = if (index != currentDirection) 1000 else 0
                queue.add(State(path + next, currentCost + 1 + turnCost, index))
            }
        }
    }

    println(solutions.first().cost)
    println(solutions.flatMap { state -> state.path }.toSet().size)
}