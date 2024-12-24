package day24

import java.io.File

data class Gate(val input1: String, val input2: String, val operation: String, val output: String)

fun main() {
    val (first, second) = File("src/main/kotlin/day24/input.txt").readText().split("\n\n")
    val wires = first.lines()
        .associate { it.split(": ").let { (wire, value) -> wire to value.toInt() } }
    val gates = second.lines().map {
        """(.*) (.*) (.*) -> (.*)""".toRegex().matchEntire(it)!!.groupValues
            .let { (_, input1, operation, input2, output) ->
                Gate(input1, input2, operation, output)
        }
    }.toSet()
    println(solve(gates, wires))

    val expected = expected(45)
    val swaps = setOf(Pair("vss", "z14"), Pair("kdh", "hjf"), Pair("z31", "kpp"), Pair("z35", "sgj"))
    expand(swap(gates, swaps), wires).toSortedMap().filter { (k, v) ->
        expected[k] != v
    }.filter { (k, v) -> expected[k] != v }.forEach { (k, v) ->
        println("Expected: ${expected[k]}")
        println("Found   : $v")
    }
    println(swaps.flatMap { listOf(it.first, it.second) }.sorted().joinToString(","))
}

fun swap(gates: Set<Gate>, swaps: Set<Pair<String, String>>): Set<Gate> {
    val result = gates.toMutableSet()
    swaps.forEach { swap ->
        val first = gates.first { it.output == swap.first }
        val second = gates.first { it.output == swap.second }
        result.remove(first)
        result.remove(second)
        result.add(first.copy(output = swap.second))
        result.add(second.copy(output = swap.first))
    }
    return result
}

private fun solve(startGates: Set<Gate>, startWires: Map<String, Int>): Long {
    val wires = startWires.toMutableMap()
    var gates = startGates
    while (gates.isNotEmpty()) {
        val (resolvableGates, unresolvableGates) = gates.partition { it.input1 in wires && it.input2 in wires }
        gates = unresolvableGates.toSet()
        resolvableGates.forEach { gate ->
            wires[gate.output] = when (gate.operation) {
                "AND" -> wires[gate.input1]!! and wires[gate.input2]!!
                "OR" -> wires[gate.input1]!! or wires[gate.input2]!!
                "XOR" -> wires[gate.input1]!! xor wires[gate.input2]!!
                else -> error("Unknown operation")
            }
        }
    }
    return toInt(wires, "z")
}

private fun expected(length: Int): Map<String, String> {
    val results = mutableMapOf<String, String>()
    var carryIn: String? = null
    for (i in 0 until length) {
        val asString = i.toString().padStart(2, '0')
        val left = sorted("x$asString", "XOR", "y$asString")
        results["z$asString"] = if (carryIn != null) sorted(left, "XOR", carryIn) else left
        val carryLeft = sorted("x$asString", "AND", "y$asString")
        carryIn =  if (carryIn != null) sorted(carryLeft, "OR", sorted(carryIn, "AND", sorted ("x$asString", "XOR", "y$asString"))) else carryLeft
    }
    results["z$length"] = carryIn!!
    return results
}

private fun sorted(left: String, op: String, right: String): String {
    val sorted = sortedSetOf(left, right)
    return "(${sorted.elementAt(0)} $op ${sorted.elementAt(1)})"
}

private fun expand(startGates: Set<Gate>, startWires: Map<String, Int>): Map<String, String> {
    val wires = startWires.map { (k, _) -> k to k }.toMap().toMutableMap()
    var gates = startGates
    while (gates.isNotEmpty()) {
        val (resolvableGates, unresolvableGates) = gates.partition { it.input1 in wires && it.input2 in wires }
        gates = unresolvableGates.toSet()
        resolvableGates.forEach { gate ->
            val sorted = sortedSetOf(wires[gate.input1], wires[gate.input2])
            wires[gate.output] = "(${sorted.elementAt(0)} ${gate.operation} ${sorted.elementAt(1)})"
        }
    }
    return wires.filter { (k, _) -> k.startsWith("z") }.toSortedMap()
}

private fun toInt(wires: MutableMap<String, Int>, prefix: String) =
    wires.filter { (k, _) -> k.startsWith(prefix) }.toList()
        .sortedByDescending { it.first }
        .fold(0L) { acc, (_, v) -> (acc * 2L) + v }
