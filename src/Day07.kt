import kotlin.math.pow

fun main() {
    val startTime = System.currentTimeMillis()

    fun getDigitCount(number: Int): Int {
        return if (number < 10) 1 else 1 + getDigitCount(number / 10)
    }

    fun getNextPowerOfTen(number: Int): Int {
        return 10.0.pow(getDigitCount(number)).toInt()
    }

    fun testCalibration(expectedResult: Long, currentValuePosition: Int, currentResult: Long, testValues: List<Int>): Boolean {
        val value = testValues[currentValuePosition]
        val multResult = currentResult * testValues[currentValuePosition]
        val plusResult = currentResult + value
        val concatResult = (currentResult * getNextPowerOfTen(value)) + value

        if (currentValuePosition == testValues.size - 1) {
            return multResult == expectedResult || plusResult == expectedResult || concatResult == expectedResult
        }

        val nextPosition = currentValuePosition + 1
        return testCalibration(expectedResult, nextPosition, multResult, testValues)
                || testCalibration(expectedResult, nextPosition, plusResult, testValues)
                || testCalibration(expectedResult, nextPosition, concatResult, testValues)
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { equation ->
            val (resultPart, testValuesPart) = equation.split(":").map { it.trim() }
            val result = resultPart.toLong()
            val testValues = testValuesPart.split(" ").map { it.toInt() }

            if (testCalibration(result, 0, 0, testValues)) result else 0
        }
    }


    val testInput = readInput("Day07_test")
//    println("Test output (part1): ${part1(testInput)}")
    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day07")
//    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
