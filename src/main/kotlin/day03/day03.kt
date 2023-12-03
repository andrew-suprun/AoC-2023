package day03

import java.io.File

fun main() {
    run(::part1)
    run(::part2)
}

data class Number(val value: Int, val row: Int, val colStart: Int, var colEnd: Int)

fun run(part: (numbers: List<Number>, char: Char, row: Int, col: Int) -> Int) {
    val lines = File("input.data").readLines()
    val numbers = parseEngine(lines)
    var result = 0

    for ((row, line) in lines.withIndex()) {
        for ((col, char) in line.withIndex()) {
            result += part(numbers, char, row, col)
        }
    }
    println(result)
}

fun part1(numbers: List<Number>, char: Char, row: Int, col: Int): Int {
    var result = 0
    if (char != '.' && !char.isDigit()) {
        for (number in numbers) {
            if (adjacent(number, row, col)) {
                result += number.value
            }
        }
    }
    return result
}

fun part2(numbers: List<Number>, char: Char, row: Int, col: Int): Int {
    var result = 0
    if (char == '*') {
        val adjacentNumbers = mutableListOf<Number>()
        for (number in numbers) {
            if (adjacent(number, row, col)) adjacentNumbers += number
        }
        if (adjacentNumbers.size == 2) {
            result += adjacentNumbers[0].value * adjacentNumbers[1].value
        }
    }
    return result
}

fun parseEngine(lines: List<String>): List<Number> {
    val numbers = mutableListOf<Number>()
    for ((row, line) in lines.withIndex()) {
        var firstDigit = -1
        for ((col, char) in line.withIndex()) {
            if (char.isDigit()) {
                if (firstDigit == -1) {
                    firstDigit = col
                }
            } else {
                if (firstDigit != -1) {
                    val value = line.substring(firstDigit, col).toInt()
                    numbers += Number(value = value, row = row, colStart = firstDigit, colEnd = col)
                    firstDigit = -1
                }
            }
        }
        if (firstDigit != -1) {
            val value = line.substring(firstDigit, line.length).toInt()
            numbers += Number(value = value, row = row, colStart = firstDigit, colEnd = line.length)
        }
    }
    return numbers
}

fun adjacent(number: Number, row: Int, col: Int): Boolean =
    row in number.row - 1..number.row + 1 && col in number.colStart - 1..number.colEnd

// Part1:   527144
// Part2: 81463996