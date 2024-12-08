package day8

import java.io.File

data class Coord(val x: Int, val y: Int) {
    operator fun minus(other: Coord) = Coord(x - other.x, y - other.y)
    operator fun plus(other: Coord) = Coord(x + other.x, y + other.y)
    operator fun times(multipler: Int) = Coord( x * multipler, y * multipler)
}

fun main() {
    val input = File("src/main/kotlin/day8/input.txt").readLines()
        .flatMapIndexed { y, line -> line.mapIndexed { x, c -> Coord(x, y) to c } }
    val validCoords = input.toMap().keys

    val antennae = input
        .filter { it.second != '.' }
        .groupBy({ it.second }, {it.first })
    val pairs = antennae.flatMap { (_, coords) ->
        coords.flatMapIndexed { indexA, coordA ->
            coords.filterIndexed { indexB, coordB ->
                indexA != indexB
            }.map { coordB -> Pair(coordA, coordB) }
        }
    }

    println(
        pairs.fold(emptySet<Coord>()) { antinodes, (coordA, coordB) ->
            val antinode = coordA + (coordA - coordB)
            if (antinode in validCoords) antinodes + antinode else antinodes
        }.size
    )

    println(
        pairs.fold(emptySet<Coord>()) { antinodes, (coordA, coordB) ->
            var coord = coordB
            antinodes + generateSequence { coord += (coordA - coordB); coord.takeIf { it in validCoords } }
        }.size
    )

}

