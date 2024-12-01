import kotlin.math.abs

fun main() {
    fun parseInput(input: List<String>): Pair<MutableList<Int>, MutableList<Int>> {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()

        for (line in input) {
            val lines: List<String> = line.split("\\s+".toRegex()).filter { it.isNotEmpty() }
            leftList.add(lines[0].toInt())
            rightList.add(lines[1].toInt())
        }

        return Pair(leftList, rightList)
    }

    fun part1(input: List<String>): Int {
        val (leftList, rightList) = parseInput(input)
    }

    fun part2(input: List<String>): Int {
        val (leftList, rightList) = parseInput(input)
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    println("Test output: part1")
    part1(testInput).println()
    println("Test output: part2")
    part2(testInput).println()
    println()

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    println("Real output: part1")
    part1(input).println()
    println("Real output: part1")
    part2(input).println()
}
