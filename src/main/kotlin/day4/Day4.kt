package day4

import java.io.File

data class Delta(val x: Int, val y: Int)

fun main() {
    val grid = File("src/main/kotlin/day4/input.txt").readLines()

    val xmas = "XMAS"
    val deltas =
        listOf(
            Delta(-1, -1),
            Delta(-1, 0),
            Delta(-1, 1),
            Delta(0, -1),
            Delta(0, 1),
            Delta(1, -1),
            Delta(1, 0),
            Delta(1, 1)
        )
    var xmasOccurrences = 0
    for (x in grid[0].indices) {
        for (y in grid.indices) {
            for (delta in deltas) {
                var found = true
                for (i in xmas.indices) {
                    val newX = x + delta.x * i
                    val newY = y + delta.y * i
                    if (newX < 0 || newX >= grid[0].length || newY < 0 || newY >= grid.size || grid[newY][newX] != xmas[i]) {
                        found = false
                        break
                    }
                }
                if (found) {
                    xmasOccurrences++
                }
            }
        }
    }
    println(xmasOccurrences)

    val corners = listOf(
        Pair(Delta(-1, -1), Delta(1, 1)),
        Pair(Delta(-1, 1), Delta(1, -1)),
        Pair(Delta(1, 1), Delta(-1, -1)),
        Pair(Delta(1, -1), Delta(-1, 1))
    )
    var masOccurrences = 0
    for (x in 1 until grid[0].length - 1) {
        for (y in 1 until grid.size - 1) {
            if (grid[y][x] == 'A' && corners.count { (a, b) -> grid[y + a.y][x + a.x] == 'M' && grid[y + b.y][x + b.x] == 'S' } == 2) {
                masOccurrences++
            }
        }
    }
    println(masOccurrences)
}