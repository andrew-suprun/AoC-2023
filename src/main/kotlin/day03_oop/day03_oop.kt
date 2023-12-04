package day03_oop

import java.io.File

fun main() {
    Part1("input.data").run()
    Part2("input.data").run()
}

data class Number(val value: Int, val row: Int, val colStart: Int, var colEnd: Int)

abstract class Day03(fileName: String) {
    val numbers = mutableListOf<Number>()
    private val lines = File("input.data").readLines()

    abstract fun score(char: Char, row: Int, col:Int): Int

    fun run() {
        var result = 0
        parseEngine()

        for ((row, line) in lines.withIndex()) {
            for ((col, char) in line.withIndex()) {
                result += score(char, row, col)
            }
        }
        println(result)
    }

    private fun parseEngine() {
        val lines = File("input.data").readLines()
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
    }

    fun adjacent(number: Number, row: Int, col: Int): Boolean =
        row in number.row - 1..number.row + 1 && col in number.colStart - 1..number.colEnd
}

class Part1(fileName: String): Day03(fileName) {
    override fun score(char: Char, row: Int, col: Int): Int {
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
}

class Part2(fileName: String): Day03(fileName) {
    override fun score(char: Char, row: Int, col: Int): Int {
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
}