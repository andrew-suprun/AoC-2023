package day08

import java.io.File

data class Targets(val left: String, val right: String)

fun main() {
    run(start = { it == "AAA" }, end = { it == "ZZZ" })
    run(start = { it.endsWith('A') }, end = { it.endsWith('Z') })
}

fun run(start: (String) -> Boolean, end: (String) -> Boolean) {
    val lines = File("input.data").readLines()
    val turns = turnSequence(lines[0])
    val instructions = parseInstructions(lines.drop(2))
    var totalSteps = 1L
    for (startPlace in instructions.keys.filter(start)) {
        var place = startPlace
        var steps = 0L
        for (turn in turns) {
            steps++
            val targets = instructions[place]!!
            place = if (turn == 'L') targets.left else targets.right

            if (end(place)) break
        }
        totalSteps = lcm(totalSteps, steps)
    }
    println(totalSteps)
}

fun parseInstructions(lines: List<String>): Map<String, Targets> {
    val result = mutableMapOf<String, Targets>()
    for (line in lines) {
        val parts = line.split(" = (", ", ", ")")
        result[parts[0]] = Targets(left = parts[1], right = parts[2])
    }
    return result
}

fun turnSequence(turns: String) = sequence {
    while (true) {
        for (char in turns) {
            yield(char)
        }
    }
}

fun lcm(first: Long, second: Long): Long = first / gcd(first, second) * second

fun gcd(first: Long, second: Long): Long {
    var a = first
    var b = second
    while (b > 0) {
        val t = b
        b = a % b
        a = t
    }
    return a
}

//          11911
// 10151663816849