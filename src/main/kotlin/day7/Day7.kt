import java.io.File

fun solve(target: Long, remainder: List<Long>, total: Long, ops: List<(Long, Long) -> Long>): Boolean {
    if (remainder.isEmpty()) {
        return target == total
    }
    if (total > target) {
        return false
    }
    return ops.any { op -> solve(target, remainder.drop(1), op(total, remainder[0]), ops) }
}

fun main() {
    val input = File("src/main/kotlin/day7/input.txt").readLines().map {
        val (left, right) = it.split(": ")
        Pair(left.toLong(), right.split(" ").map(String::toLong))
    }

    println(input.filter {
        solve(it.first, it.second.drop(1), it.second[0], listOf(Long::plus, Long::times))
    }.sumOf { it.first })

    println(input.filter {
        solve(it.first, it.second.drop(1), it.second[0],
            listOf(Long::plus, Long::times, { a, b -> "$a$b".toLong() })
        )
    }.sumOf { it.first })
}