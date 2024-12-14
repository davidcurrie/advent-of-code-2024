package day14

import java.io.File

data class Coord(val x: Int, val y: Int)

fun mod(a: Int, b: Int) = (a % b + b) % b

fun safetyFactor(width: Int, height: Int, robots: List<Coord>) : Int {
    return robots.map { coord ->
        when {
            coord.x < width / 2 && coord.y < height / 2 -> 1
            coord.x < width / 2 && coord.y > height / 2 -> 2
            coord.x > width / 2 && coord.y < height / 2 -> 3
            coord.x > width / 2 && coord.y > height / 2 -> 4
            else -> 0
        }
    }.groupBy { it }.minus(0).values.fold(1) { acc, list -> acc * list.size }
}

fun main() {
    val width = 101
    val height = 103
    val seconds = 100

    val input = File("src/main/kotlin/day14/input.txt").readLines().map { line ->
        """p=(\d+),(\d+) v=(-?\d+),(-?\d+)""".toRegex().find(line)!!.destructured.let { (x, y, vx, vy) ->
            Coord(x.toInt(), y.toInt()) to Coord(vx.toInt(), vy.toInt())
        }
    }
    println(safetyFactor(width, height, input.map { (start, velocty) ->
        Coord(mod(start.x + (seconds * velocty.x), width), mod(start.y + (seconds * velocty.y), height))
    }))

    var time = 0
    var robots = input.map { it.first }
    val velocities = input.map { it.second }
    // Look for horizontal lines of robots
    do {
        time++
        robots = robots.mapIndexed { index, robot -> Coord(mod(robot.x + velocities[index].x, width), mod(robot.y + velocities[index].y, height)) }
    } while (robots.none { robot -> (0..7).all { Coord(robot.x + it, robot.y) in robots } })

    (0 until height).forEach { y ->
        (0 until width).forEach { x ->
            print(if (Coord(x, y) in robots) '#' else '.')
        }
        println()
    }
    println(time)
}