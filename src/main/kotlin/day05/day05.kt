package day05

import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    run(::part1)
    run(::part2)
}

data class Map(val range: LongRange, val inc: Long)
data class Model(val seeds: List<LongRange>, val allMaps: List<List<Map>>)

fun run(part: (String) -> List<LongRange>) {
    val model = parseInput(part)
    var mapped = model.seeds
    for (maps in model.allMaps) {
        mapped = mapLongRanges(mapped, maps)
    }
    println(mapped.minOfOrNull { it.first })
}

fun mapLongRanges(ranges: List<LongRange>, maps: List<Map>): List<LongRange> {
    val result = mutableListOf<LongRange>()
    var unprocessed = mutableListOf<LongRange>()
    var toProcess = MutableList(ranges.size) { ranges[it] }
    for (map in maps) {
        for (range in toProcess) {
            if (range.first >= map.range.last || range.last <= map.range.first) {
                unprocessed += range
            } else {
                val start = max(range.first, map.range.first)
                val end = min(range.last, map.range.last)
                result += start + map.inc..<end + map.inc
                if (range.first < start) {
                    unprocessed += range.first..<start
                }
                if (range.last > end) {
                    unprocessed += end..<range.last
                }
            }
        }

        toProcess = unprocessed
        unprocessed = mutableListOf()
    }

    result += toProcess
    return result
}

fun parseInput(seeds: (String) -> List<LongRange>): Model {
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
                curMaps += Map(range = numbers[1]..< numbers[1] + numbers[2], inc = numbers[0] - numbers[1])
            }
        }
    }
    allMaps += curMaps
    return Model(seeds = ranges, allMaps = allMaps)
}

fun part1(line: String): List<LongRange> =
    line.split(" ").drop(1).map {
        val start = it.toLong()
        start..<start+1
    }

fun part2(line: String): List<LongRange> {
    val seedNumbers = line.split(" ").drop(1).map { it.toLong() }
    return seedNumbers.chunked(2).map { (start, len) -> start..<start + len }
}

// 993500720
//   4917124