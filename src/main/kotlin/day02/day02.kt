package day02

import java.io.File
import kotlin.math.max

data class Game(val id: Int, val colors: Colors)
data class Colors(val red: Int, val green: Int, val blue: Int)

fun main() {
    println(run(::part1))
    println(run(::part2))
}

fun run(scoreGame: (Game) -> Int): Int = File("input.data").readLines().sumOf { scoreGame(parseGame(it)) }

fun parseGame(line: String): Game {
    val (gameIdStr, gameDataStr) = line.split(": ")
    val gameId = gameIdStr.split(" ")[1].toInt()
    return Game(id = gameId, colors = parseColors(gameDataStr))
}

fun parseColors(gameDataStr: String): Colors {
    var red = 0
    var green = 0
    var blue = 0
    for (setStr in gameDataStr.split("; ")) {
        val colorsStr = setStr.split(", ")
        for (colorStr in colorsStr) {
            val (cubesStr, color) = colorStr.split(" ")
            val cubes = cubesStr.toInt()
            when (color) {
                "red" -> red = max(red, cubes)
                "green" -> green = max(green, cubes)
                "blue" -> blue = max(blue, cubes)
            }
        }
    }
    return Colors(red = red, green = green, blue = blue)
}

fun part1(game: Game): Int {
    val colors = game.colors
    if (colors.red <= 12 && colors.green <= 13 && colors.blue <= 14) return game.id
    return 0
}

fun part2(game: Game): Int {
    return game.colors.red * game.colors.green * game.colors.blue
}    