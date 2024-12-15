package day15

import java.io.File

val directions = mapOf(
    '^' to Coord(0, -1),
    '>' to Coord(1, 0),
    'v' to Coord(0, 1),
    '<' to Coord(-1, 0)
)

data class Coord(val x: Int, val y: Int) {
    operator fun plus(delta: Coord) = Coord(x + delta.x, y + delta.y)
    operator fun times(multiplier: Int) = Coord(x * multiplier, y)
}

data class Feature(val coord: Coord, val width: Int = 1) {
    fun overlaps(other: Feature) =
        coord.y == other.coord.y &&
                coord.x < (other.coord.x + other.width) &&
                coord.x + width > other.coord.x
    operator fun plus(delta: Coord) = copy(coord = Coord(coord.x + delta.x, coord.y + delta.y))
    operator fun times(multiplier: Int) = Feature(coord = coord * multiplier, width = width * multiplier)
}

fun main() {
    val (first, second) = File("src/main/kotlin/day15/input.txt").readText()
        .split("\n\n").map { it.lines() }
    val map = first.mapIndexed { y, line ->
        line.mapIndexed { x, c -> Coord(x, y) to c }
    }.flatten().toMap()

    val robotStart = Feature(map.filterValues { it == '@' }.keys.first())
    val walls = map.filterValues { it == '#' }.keys.map { Feature(Coord(it.x, it.y)) }.toSet()
    val boxesStart = map.filterValues { it == 'O' }.keys.map { Feature(Coord(it.x, it.y)) }.toSet()
    val moves = second.joinToString("").map { directions[it]!! }

    println(solve(robotStart, boxesStart, walls, moves))
    println(solve(
        Feature(robotStart.coord * 2),
        boxesStart.map { it * 2 }.toSet(),
        walls.map { it * 2 }.toSet(),
        moves
    ))
}

private fun solve(robotStart: Feature, boxesStart: Set<Feature>, walls: Set<Feature>, moves: List<Coord>): Int {
    var robot = robotStart
    var boxes = boxesStart
    moves.forEach { direction ->
        val nextRobot = robot + direction
        val unmovedBoxes = boxes.toMutableSet()
        val movedBoxes = mutableSetOf<Feature>()
        var previousMoves = setOf(nextRobot)
        do {
            if (previousMoves.any { feature -> walls.any { wall -> feature.overlaps(wall) } }) {
                return@forEach
            }
            val overlapping = unmovedBoxes
                .filter { box -> previousMoves.any { feature -> feature.overlaps(box) } }
                .toSet()
            unmovedBoxes -= overlapping
            previousMoves = overlapping.map { box -> box + direction }.toSet()
            movedBoxes += previousMoves
        } while (overlapping.isNotEmpty())
        robot = nextRobot
        boxes = unmovedBoxes + movedBoxes
    }
    return boxes.sumOf { it.coord.x + 100 * it.coord.y }
}
