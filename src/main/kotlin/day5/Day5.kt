package day5

import java.io.File

fun main() {
    val (ruleLines, updateLines) = File("src/main/kotlin/day5/input.txt").readText().split("\n\n").map { it.split("\n") }
    val rules = ruleLines.map { it.split("|").map(String::toInt) }.groupBy { it[0] }.mapValues { (_, v) -> v.map { it[1] } }
    val updates = updateLines.map { it.split(",").map(String::toInt) }
    val (ordered, unordered) = updates.partition { update ->
        update.all { page ->
            rules[page]?.filter { it in update }?.all { update.indexOf(it) > update.indexOf(page) } ?: true
        }
    }

    println(ordered.sumOf { it[it.size / 2] })

    println(unordered.sumOf { pages ->
        val remainingPages = pages.toMutableSet()
        val orderedPages = mutableListOf<Int>()
        while (remainingPages.isNotEmpty()) {
            val next = remainingPages.first { page -> rules[page]?.none { remainingPages.contains(it) } ?: true }
            orderedPages.add(0, next)
            remainingPages.remove(next)
        }
        orderedPages[orderedPages.size / 2]
    })
}