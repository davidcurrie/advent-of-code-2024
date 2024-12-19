package day19

import java.io.File

fun main() {
    val (first, designs) = File("src/main/kotlin/day19/input.txt").readText().split("\n\n")
    val towels = first.split(", ").toSet()

    val memo = mutableMapOf<String, Long>()
    println(designs.lines().count { options(it, towels, memo) > 0 })
    println(designs.lines().sumOf { options(it, towels, memo) })
}

fun options(design: String, towels: Set<String>, memo: MutableMap<String, Long>): Long =
    memo[design] ?: (
            if (design.isEmpty()) 1L
            else towels.sumOf {
                towel -> if (design.startsWith(towel)) options(design.removePrefix(towel), towels, memo) else 0L
            })
        .also { memo[design] = it }
