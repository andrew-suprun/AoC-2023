package day04

import java.io.File

fun main() {
    Part1("input.data").run()
    Part2("input.data").run()
}

data class Card(val matchingNumbers: Int, var copies: Int = 1)

abstract class Day04(val fileName: String) {
    val cards = parseInput()

    fun run() {
        var result = 0

        for ((idx, card) in cards.withIndex()) {
            for (idx2 in idx + 1..idx + card.matchingNumbers) {
                cards[idx2].copies += card.copies
            }
            result += score(card)
        }

        println(result)
    }

    abstract fun score(card: Card): Int

    private fun parseInput(): List<Card> {
        val cards = mutableListOf<Card>()
        for (line in File(fileName).readLines()) {
            val parts = line.split(": ", " | ")
            cards += Card(matchingNumbers = parseNumbers(parts[1]).intersect(parseNumbers(parts[2])).size)
        }
        return cards
    }

    private fun parseNumbers(numbers: String) = numbers.trim().split(Regex(" +")).toSet()
}

class Part1(fileName: String) : Day04(fileName) {
    override fun score(card: Card): Int {
        if (card.matchingNumbers == 0) return 0
        var result = 1
        repeat(card.matchingNumbers - 1) { result *= 2 }
        return result
    }
}

class Part2(fileName: String) : Day04(fileName) {
    override fun score(card: Card): Int = card.copies
}

// Part1:    25004
// Part2: 14427616