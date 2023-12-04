package day04_proc

import java.io.File

data class Card(val matchingNumbers: Int, var copies: Int = 1)

fun main() {
    run { card ->
        if (card.matchingNumbers == 0) 0
        else {
            var result = 1
            repeat(card.matchingNumbers - 1) { result *= 2 }
            result
        }
    }
    run { card -> card.copies }
}

fun run(score: (Card) -> Int) {
    var result = 0

    val cards = parseInput()
    for ((idx, card) in cards.withIndex()) {
        for (idx2 in idx + 1..idx + card.matchingNumbers) {
            cards[idx2].copies += card.copies
        }
        result += score(card)
    }

    println(result)
}

fun parseInput(): List<Card> {
    val cards = mutableListOf<Card>()
    for (line in File("input.data").readLines()) {
        val parts = line.split(": ", " | ")
        cards += Card(matchingNumbers = parseNumbers(parts[1]).intersect(parseNumbers(parts[2])).size)
    }
    return cards
}

fun parseNumbers(numbers: String) = numbers.trim().split(Regex(" +")).toSet()

// Part1:    25004
// Part2: 14427616