package day05

import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    run(::part1)
    run(::part2)
}

data class Range(val start: Long, val end: Long)
data class Map(val range: Range, val inc: Long)
data class Model(val seeds: List<Range>, val allMaps: List<List<Map>>)

fun run(part: (String) -> List<Range>) {
    val model = parseInput(part)
    var mapped = model.seeds
    for (maps in model.allMaps) {
        mapped = mapRanges(mapped, maps)
    }
    println(mapped.minOfOrNull { it.start })
}

fun mapRanges(ranges: List<Range>, maps: List<Map>): List<Range> {
    val result = mutableListOf<Range>()
    var unprocessed = mutableListOf<Range>()
    var toProcess = MutableList(ranges.size) { ranges[it] }
    for (map in maps) {
        for (range in toProcess) {
            if (range.start >= map.range.end || range.end <= map.range.start) {
                unprocessed += range
            } else {
                val start = max(range.start, map.range.start)
                val end = min(range.end, map.range.end)
                result += Range(start = start + map.inc, end = end + map.inc)
                if (range.start < start) {
                    unprocessed += Range(start = range.start, end = start)
                }
                if (range.end > end) {
                    unprocessed += Range(start = end, end = range.end)
                }
            }
        }

        toProcess = unprocessed
        unprocessed = mutableListOf()
    }

    result += toProcess
    return result
}

fun parseInput(seeds: (String) -> List<Range>): Model {
    val lines = File("input.data").readLines()
    val ranges = seeds(lines[0])


    val allMaps = mutableListOf<List<Map>>()
    var curMaps = mutableListOf<Map>()

    for (line in lines.drop(3)) {
        when {
            line == "" -> continue
            line.endsWith(" map:") -> {
                allMaps += curMaps
                curMaps = mutableListOf()
            }

            else -> {
                val numbers = line.split(" ").map { it.toLong() }
                curMaps += Map(Range(start = numbers[1], end = numbers[1] + numbers[2]), inc = numbers[0] - numbers[1])
            }
        }
    }
    allMaps += curMaps
    return Model(seeds = ranges, allMaps = allMaps)
}

fun part1(line: String): List<Range> =
    line.split(" ").drop(1).map {
        val start = it.toLong()
        Range(start = start, end = start+1)
    }

fun part2(line: String): List<Range> {
    val seedNumbers = line.split(" ").drop(1).map { it.toLong() }
    return seedNumbers.chunked(2).map { (start, len) -> Range(start = start, end = start + len) }
}

// 993500720
//   4917124