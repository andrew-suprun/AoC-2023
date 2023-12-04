package day04

fun main() {
    Part1("test.data").run()
    Part2("test.data").run()
}

abstract class Day04(fileName: String) {
    fun run() {}
}

class Part1(fileName: String) : Day04(fileName) {

}

class Part2(fileName: String) : Day04(fileName) {

}
