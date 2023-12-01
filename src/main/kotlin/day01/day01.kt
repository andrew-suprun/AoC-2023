import java.io.File

fun main(args: Array<String>) {
    println(run(digitsPart1))
    println(run(digitsPart2))
}

fun run(digits: Map<String, Int>) = File("input.data")
        .readLines()
        .map { findDigits(it, digits) }
        .sumOf { it.first() * 10 + it.last() }

fun findDigits(line: String, digits: Map<String, Int>): List<Int> {
    val result = mutableListOf<Int>()
    for (i in line.indices) {
        for ((text, digit) in digits) {
            if (i + text.length <= line.length && line.substring(i..<i + text.length) == text) {
                result += digit
            }
        }
    }
    return result
}

val digitsPart1 = mapOf(
        "0" to 0,
        "1" to 1,
        "2" to 2,
        "3" to 3,
        "4" to 4,
        "5" to 5,
        "6" to 6,
        "7" to 7,
        "8" to 8,
        "9" to 9,
)
val digitsPart2 = mapOf(
        "zero" to 0,
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
        "0" to 0,
        "1" to 1,
        "2" to 2,
        "3" to 3,
        "4" to 4,
        "5" to 5,
        "6" to 6,
        "7" to 7,
        "8" to 8,
        "9" to 9,
)