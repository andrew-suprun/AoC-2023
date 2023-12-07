package day07

import java.io.File

data class Bid(val hand: String, val bet: Int)

fun main() {
    run(encode1, ::part1)
    run(encode2, ::part2)
}

fun run(encode: Map<Char, Char>, rank: (String) -> Char) {
    val result = File("input.data").readLines().map { parseHand(it, encode, rank) }.sortedBy { it.hand }.withIndex()
        .sumOf { (idx, card) -> (idx + 1) * card.bet }
    println(result)
}

fun parseHand(line: String, encode: Map<Char, Char>, type: (String) -> Char): Bid {
    val (handStr, bet) = line.split(" ")
    val hand = handStr.map { encode[it] }.joinToString(separator = "")
    return Bid(hand = type(handStr) + hand, bet = bet.toInt())
}

fun part1(hand: String): Char {
    val sizes = hand.map { it }.groupBy { it }.map { it.value }.map { it.size }.sortedBy { -it }
    return type(sizes)
}

fun part2(hand: String): Char {
    val g = hand.map { it }.groupBy { it }.map { it.value }.toList().sortedBy { -it.size }.toList()
    var bestRank = g[0][0]
    if (bestRank == 'J' && g.size > 1) {
        bestRank = g[1][0]
    }
    val sizes =
        hand.replace('J', bestRank).map { it }.groupBy { it }.map { it.value }.map { it.size }.sortedBy { -it }
            .toList()

    return type(sizes)
}

fun type(sizes: List<Int>): Char = when (sizes[0]) {
    1 -> 'a'        // high card
    2 -> when (sizes[1]) {
        1 -> 'b'    // pair
        else -> 'c' // two pair
    }

    3 -> when (sizes[1]) {
        1 -> 'd'    // three of a kind
        else -> 'e' // full house
    }

    4 -> 'f'        // four of a kind
    5 -> 'g'        // 5 aces?
    else -> 'x'
}

// encode rank to sortable code
val encode1 = mapOf(
    '2' to 'b',
    '3' to 'c',
    '4' to 'd',
    '5' to 'e',
    '6' to 'f',
    '7' to 'g',
    '8' to 'h',
    '9' to 'i',
    'T' to 'j',
    'J' to 'k',
    'Q' to 'l',
    'K' to 'm',
    'A' to 'n',
)

// encode rank to sortable code
val encode2 = mapOf(
    'J' to 'a',
    '2' to 'b',
    '3' to 'c',
    '4' to 'd',
    '5' to 'e',
    '6' to 'f',
    '7' to 'g',
    '8' to 'h',
    '9' to 'i',
    'T' to 'j',
    'Q' to 'l',
    'K' to 'm',
    'A' to 'n',
)

//251927063
//255632664