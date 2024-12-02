package day2

import java.io.File

fun main() {
    val reports = File("src/main/kotlin/day2/input.txt")
        .readLines()
        .map { it.split(" ").map(String::toInt) }

    println(reports.count { it.isSafe() })
    println(reports.count { it.isSafe() || it.dampen().any { it.isSafe() } })
}

private fun List<Int>.isSafe() =
    this.zipWithNext().map { (a, b) -> a - b }.let {
        it.all { it in 1..3 } || it.all { it in -3..-1 }
    }

private fun <T> List<T>.dampen() =
    List(this.size - 1) { index -> this.filterIndexed { i, _ -> i != index } }
