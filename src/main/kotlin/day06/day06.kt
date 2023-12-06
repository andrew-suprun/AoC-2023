package day06

import java.io.File

data class Race(val time: Long, val distance: Long)

fun main() {
    run(::part1)
    run(::part2)
}

fun run(part: (String) -> List<Long>) {
    val lines = File("input.data").readLines()
    val solution = part(lines[0]).zip(part(lines[1])).map { (time, distance) -> Race(time = time, distance = distance) }
        .map { solve(it) }.reduce { acc, n -> acc * n }
    println(solution)
}

fun part1(line: String): List<Long> = line.split(Regex(" +")).drop(1).map { it.toLong() }

fun part2(line: String): List<Long> = listOf(line.split(Regex(" +")).drop(1).joinToString(separator = "").toLong())

fun solve(race: Race): Long {
    var min = 0L
    var max = race.time
    while (true) {
        val mid = (min + max + 1) / 2
        val prod = mid * (race.time - mid)
        if (prod <= race.distance) {
            min = mid
        } else {
            if (max - min <= 1) {
                return race.time + 1 - 2 * mid
            }
            max = mid
        }
    }
}

//  2449062
// 33149631