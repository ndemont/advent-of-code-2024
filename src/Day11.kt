import kotlin.math.pow

fun main() {
    val startTime = System.currentTimeMillis()

    fun getDigitCount(number: Long): Int {
        return if (number < 10) 1 else 1 + getDigitCount(number / 10)
    }

    fun applyRule(stone: Long): Pair<Long, Long>{
        if (stone == 0.toLong()) {
            return 1.toLong() to (-1).toLong()
        }

        val stoneSize = getDigitCount(stone)
        if (stoneSize % 2 == 0) {
            val powerOfTen = 10.0.pow(stoneSize / 2).toLong()
            val rightStone = stone % powerOfTen
            val leftStone = stone / powerOfTen

            return leftStone to rightStone
        }

        return stone * 2024 to (-1).toLong()
    }

    fun part1(input: List<String>): Int {
        var stones = input.first().split(" ").map { it.toLong() }

        repeat(25) {
            val newStones = mutableListOf<Long>()

            stones.forEach { stone ->
                val (leftStone, rightStone) = applyRule(stone)
                newStones.add(leftStone)
                if (rightStone >= 0) newStones.add(rightStone)
            }

            stones = newStones
        }

        return stones.size
    }

    val stoneBook = mutableMapOf<Triple<Long, Int, Long>, Long>()

    fun blink(stone: Long, times: Int, count: Long): Long {
        val pattern = Triple(stone, times, count)

        if (pattern in stoneBook) { return stoneBook[pattern]!! }
        if (times == 0) return count

        val (leftStone, rightStone) = applyRule(stone)
        val countLeft = blink(leftStone, times - 1, count)
        val newCountRight = if (rightStone >= 0) blink(rightStone, times - 1, count) else 0

        val countSum = countLeft + newCountRight
        stoneBook[pattern] = countSum
        return countSum
    }

    fun part2(input: List<String>): Long {
        val stones = input.first().split(" ").map { it.toLong() }
        var stoneCount: Long = 0

        stones.forEach { stone -> stoneCount += blink(stone, 75, 1) }

        return stoneCount
    }


    val testInput = readInput("Day11_test")
    println("Test output (part1): ${part1(testInput)}")
    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day11")
    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
