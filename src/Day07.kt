import kotlin.math.pow

fun main() {
    val startTime = System.currentTimeMillis()

    fun getDigitCount(number: Int): Int {
        return if (number < 10) 1 else 1 + getDigitCount(number / 10)
    }

    fun getPowerOfTen(number: Int): Int {
        return 10.0.pow(getDigitCount(number)).toInt()
    }

    fun testCalibration(target: Long, index: Int, current: Long, numbers: List<Int>): Boolean {
        val newIndex = index + 1
        if (newIndex == numbers.size) return target == current

        val number = numbers[newIndex]
        return testCalibration(target, newIndex, (current * number), numbers)
                || testCalibration(target, newIndex, (current + number), numbers)
                || testCalibration(target, newIndex, (current * getPowerOfTen(number)) + number, numbers)
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { equation ->
            val (resultPart, testValuesPart) = equation.split(":").map { it.trim() }
            val result = resultPart.toLong()
            val numbers = testValuesPart.split(" ").map { it.toInt() }

            if (testCalibration(result, 0, numbers[0].toLong(), numbers)) result else 0
        }
    }

//    val testInput = readInput("Day07_test")
//    println("Test output (part1): ${part1(testInput)}")
//    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day07")
//    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
