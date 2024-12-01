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
        var sum: Int = 0

        while (leftList.isNotEmpty()) {
            val leftMinWithIndex = leftList.withIndex().minByOrNull { it.value }
            val leftMin: Int = leftMinWithIndex?.value ?: 0
            val leftMinIndex: Int = leftMinWithIndex?.index ?: -1

            val rightMinWithIndex = rightList.withIndex().minByOrNull { it.value }
            val rightMin: Int = rightMinWithIndex?.value ?: 0
            val rightMinIndex: Int = rightMinWithIndex?.index ?: -1

            sum += abs(leftMin - rightMin)

            leftList.removeAt(leftMinIndex)
            rightList.removeAt(rightMinIndex)
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        val (leftList, rightList) = parseInput(input)
        var sum: Int = 0

        for (number in leftList) {
            val multiplicative: Int = rightList.count { it == number }

            sum += (number * multiplicative)
        }

        return sum
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
