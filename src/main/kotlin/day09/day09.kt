package day09

import java.io.File

fun main() {
    val result  = File("input.data").readLines().map { line -> line.split(" ").map { it.toLong() }.toMutableList() }
        .map { solve(it)}.reduce { acc, pair ->  acc.first+pair.first to acc.second+pair.second}
    println("Part1: ${result.first}\nPart2: ${result.second}")
}

fun solve(history: MutableList<Long>): Pair<Long, Long> {
    var result1 = 0L
    var result2 = 0L
    var sign = 1L
    while (history.any {it != 0L}) {
        result1 += history.last()
        result2 += history.first() * sign
        sign = -sign
        for ((i, v) in history.windowed(2).withIndex()) {
            history[i] = v[1] - v[0]
        }
        history.removeLast()
    }
    return result1 to result2
}

// Part1: 1953784198
// Part2:        957