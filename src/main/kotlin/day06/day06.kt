package day06

import java.io.File
import kotlin.math.ceil
import kotlin.math.sqrt

data class Race(val time: Long, val distance: Long)

fun main() {
    run(::part1, ::solve)
    run(::part1, ::altSolve)
    run(::part2, ::solve)
    run(::part2, ::altSolve)
}

fun run(part: (String) -> List<Long>, solution: (Race) -> Long) {
    val lines = File("input.data").readLines()
    val result = part(lines[0]).zip(part(lines[1]))
        .map { (time, distance) -> Race(time = time, distance = distance) }
        .map { solution(it) }
        .reduce { acc, n -> acc * n }

    println(result)
}

fun part1(line: String): List<Long> = line.split(Regex(" +")).drop(1).map { it.toLong() }

fun part2(line: String): List<Long> = listOf(line.split(Regex(" +")).drop(1).joinToString(separator = "").toLong())

fun solve(race: Race): Long {
    var min = 0L
    var max = race.time / 2
    while (true) {
        val mid = (min + max + 1) / 2
        val prod = mid * (race.time - mid)
        if (prod <= race.distance) {
            min = mid
        } else {
            max = mid
        }
        if (max - min == 1L) {
            return race.time + 1L - 2L * max
        }
    }
}

fun altSolve(race: Race): Long {
    val discriminant = (race.time*race.time - 4*race.distance).toDouble()
    val solution = (race.time - sqrt(discriminant)) / 2
    val roundedUp = ceil(solution).toLong()
    return race.time + 1L - 2L * roundedUp
}

//  2449062
// 33149631