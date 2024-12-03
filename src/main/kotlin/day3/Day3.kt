package day3

import java.io.File

fun main() {
    val input = File("src/main/kotlin/day3/input.txt").readText()

    println("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)".toRegex()
        .findAll(input)
        .sumOf { it.groupValues[1].toInt() * it.groupValues[2].toInt() }
    )

    println("(do|don't)\\(\\)|(mul)\\(([0-9]{1,3}),([0-9]{1,3})\\)".toRegex()
        .findAll(input)
        .fold(Pair(true, 0)) { (enabled, sum), match ->
            when (match.groupValues[1]) {
                "do" -> Pair(true, sum)
                "don't" -> Pair(false, sum)
                else -> Pair(enabled, sum + if (enabled) match.groupValues[3].toInt() * match.groupValues[4].toInt() else 0)
            }
        }.second
    )
}