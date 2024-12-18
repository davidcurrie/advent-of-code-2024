package day18

import java.io.File

data class Coord(val x: Int, val y: Int) {
    fun neighbours(size: Int) = listOf(
        Coord(x - 1, y),
        Coord(x + 1, y),
        Coord(x, y - 1),
        Coord(x, y + 1),
    ).filter { it.x in 0 until size && it.y in 0 until size }
}

fun main() {
    val bytes = File("src/main/kotlin/day18/input.txt").readLines()
        .map { it.split(",").let { Coord(it[0].toInt(), it[1].toInt()) } }
    val size = 71

    println(findPath(size, bytes.take(1024)))

    var low = 1024
    var high = bytes.size
    var result: Coord? = null
    while (low < high) {
        val mid = (low + high) / 2
        if (findPath(size, bytes.take(mid)) == null) {
            high = mid
            result = bytes[mid - 1]
        } else {
            low = mid + 1
        }
    }
    result?.let { println("${it.x},${it.y}") }
}

private fun findPath(size: Int, corrupted: List<Coord>) : Int? {
    val start = Coord(0, 0)
    val exit = Coord(size - 1, size - 1)

    val queue = ArrayDeque(listOf(Pair(start, 0)))
    val visited = mutableSetOf(start)
    while (queue.isNotEmpty()) {
        val (current, steps) = queue.removeFirst()
        if (current == exit) {
            return steps
        }
        current.neighbours(size)
            .filter { it !in visited && it !in corrupted }
            .forEach {
                visited.add(it)
                queue.add(Pair(it, steps + 1))
            }
    }
    return null
}