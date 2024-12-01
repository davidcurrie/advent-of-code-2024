package day1

import java.io.File
import kotlin.math.abs

fun main() {
    val (firstColumn, secondColumn) = File("src/main/kotlin/day1/input.txt")
        .readLines()
        .map { it.split("   ").map(String::toInt).let { Pair(it[0], it[1]) } }
        .unzip()

    println(firstColumn.sorted().zip(secondColumn.sorted()).sumOf { (a, b) -> abs(b - a) })
    println(firstColumn.sumOf { first -> secondColumn.count { second -> first == second } * first })
}