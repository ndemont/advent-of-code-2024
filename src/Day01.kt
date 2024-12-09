import kotlin.math.abs

fun main() {
    val startTime = System.currentTimeMillis()

    fun parseInput(input: List<String>): Pair<MutableList<Int>, MutableList<Int>> {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()

        input.forEach { line ->
            val (left, right) = line.split("\\s+".toRegex()).map { it.toIntOrNull() }
            if (left != null && right != null) {
                leftList.add(left)
                rightList.add(right)
            }
        }

        return leftList to rightList
    }

    fun part1(input: List<String>): Int {
        val (leftList, rightList) = parseInput(input)
        var sum = 0

        val leftListSorted = leftList.sorted().toMutableList()
        val rightListSorted = rightList.sorted().toMutableList()

        while (leftListSorted.isNotEmpty() && rightListSorted.isNotEmpty()) {
            val leftMin = leftListSorted.removeAt(0)
            val rightMin = rightListSorted.removeAt(0)
            sum += abs(leftMin - rightMin)
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        val (leftList, rightList) = parseInput(input)

        val rightListFrequency = rightList.groupingBy { it }.eachCount()

        return leftList.sumOf { number ->
            val multiplicative = rightListFrequency[number] ?: 0
            number * multiplicative
        }
    }

    val testInput = readInput("Day01_test")
    println("Test output (part1): ${part1(testInput)}")
    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day01")
    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
