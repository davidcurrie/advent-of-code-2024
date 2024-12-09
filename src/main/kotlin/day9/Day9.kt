package day9

fun main() {
    val input = java.io.File("src/main/kotlin/day9/input.txt").readText()
    println(part1(input))
    println(part2(input))
}

private fun part1(input: String): Long {

    val disk = input.flatMapIndexed { index, size ->
        val isFile = index % 2 == 0
        val id = index / 2
        List(size.digitToInt()) { if (isFile) id else null }
    }.toMutableList()

    var lastBlock = disk.size - 1
    var sum = 0L
    for (i in 0 until disk.count { it != null } ) {
        while (disk[lastBlock] == null) {
            lastBlock--
        }
        if (disk[i] != null) {
            sum += i * disk[i]!!
        } else {
            sum += i * disk[lastBlock]!!
            lastBlock--
        }
    }
    return sum
}

data class File(val start: Int, val size: Int, val id: Int)
data class Space(val start: Int, val size: Int)

private fun part2(input: String): Long {
    val spaces = mutableListOf<Space>()
    val files = mutableListOf<File>()
    var diskIndex = 0
    input.map { it.digitToInt() }.forEachIndexed { index, size ->
        if (index % 2 == 0) {
            files.add(File(diskIndex, size, index / 2))
        } else {
            spaces.add(Space(diskIndex, size))
        }
        diskIndex += size
    }

    val newFiles = mutableListOf<File>()
    files.reversed().forEach { file ->
        val spaceIndex = spaces.indexOfFirst { it.size >= file.size && it.start < file.start }
        if (spaceIndex != -1) {
            newFiles.add(file.copy(start = spaces[spaceIndex].start))
            spaces[spaceIndex] = Space(spaces[spaceIndex].start + file.size, spaces[spaceIndex].size - file.size)
        } else {
            newFiles.add(file)
        }
    }
    return newFiles.sumOf { file -> ((file.start + ((file.size - 1) / 2.0)) * file.size * file.id).toLong() }
}