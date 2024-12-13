package day12

import java.io.File

typealias Region = Set<Coord>

enum class Direction { NORTH, EAST, SOUTH, WEST }
data class Edge(val from: Coord, val to: Coord, val facing: Direction)

data class Coord(val x: Int, val y: Int) {
    fun neighbours() = setOf(Coord(x - 1, y), Coord(x + 1, y), Coord(x, y - 1), Coord(x, y + 1))
    fun edges() = setOf(
        Edge(Coord(x, y), Coord(x + 1, y), Direction.NORTH),
        Edge(Coord(x, y), Coord(x, y + 1), Direction.WEST),
        Edge(Coord(x, y + 1), Coord(x + 1, y + 1), Direction.SOUTH),
        Edge(Coord(x + 1, y), Coord(x + 1, y + 1), Direction.EAST)
    )
}

fun Region.edges() =
    this.flatMap { it.edges() }.groupBy { Pair(it.from, it.to) }.filter { it.value.size == 1 }.values.flatten().toSet()

fun Set<Edge>.toSides(): Set<Edge> {
    val horizontal = filter { it.from.y == it.to.y }
    val vertical = filter { it.from.x == it.to.x }
    return horizontal
        .groupBy { it.from.y }
        .values
        .flatMap { edges ->
            edges.sortedBy { it.from.x }
                .fold(emptyList<Edge>()) { acc, edge ->
                    if (acc.isNotEmpty() && acc.last().to.x == edge.from.x && acc.last().facing == edge.facing) {
                        acc.dropLast(1) + Edge(acc.last().from, edge.to, edge.facing)
                    } else {
                        acc + edge
                    }
                }
        }.toSet() + vertical
        .groupBy { it.from.x }
        .values
        .flatMap { edges ->
            edges.sortedBy { it.from.y }
                .fold(emptyList<Edge>()) { acc, edge ->
                    if (acc.isNotEmpty() && acc.last().to.y == edge.from.y && acc.last().facing == edge.facing) {
                        acc.dropLast(1) + Edge(acc.last().from, edge.to, edge.facing)
                    } else {
                        acc + edge
                    }
                }
        }.toSet()
}

fun findRegion(visited: MutableSet<Coord>, map: Map<Coord, Char>, coord: Coord): Region {
    if (coord in visited) return emptySet()
    visited.add(coord)
    val neighbours = coord.neighbours().filter { coord in map }.filter { neighbour -> map[coord] == map[neighbour] }
    return neighbours.flatMap { neighbour -> findRegion(visited, map, neighbour) }.toSet() + coord
}

fun main() {
    val map = File("src/main/kotlin/day12/input.txt").readLines()
        .flatMapIndexed { y, line -> line.mapIndexed { x, c -> Coord(x, y) to c } }.toMap()

    val visited = mutableSetOf<Coord>()
    val regions = map.keys.map { coord -> findRegion(visited, map, coord) }.filter { it.isNotEmpty() }
    println(regions.sumOf { region -> region.size * region.edges().size })
    println(regions.sumOf { region -> region.size * region.edges().toSides().size })
}