import kotlin.math.pow

fun main() {
    val startTime = System.currentTimeMillis()

    fun getNextPowerOfTen(number: Int): Int {
        val length = number.toString().length
        return 10.0.pow(length).toInt()
    }

    fun testCalbration(expectedResult: Long, currentValuePosition: Int, currentResult: Long, testValues: List<Int> ): Long {
        val newMultResult = currentResult * testValues[currentValuePosition]
        val newPlusResult = currentResult + testValues[currentValuePosition]
        val newConcatResult = (currentResult * getNextPowerOfTen(testValues[currentValuePosition])) + testValues[currentValuePosition]

        if (currentValuePosition == testValues.size - 1) {
            if (newMultResult == expectedResult) { return newMultResult }
            if (newPlusResult == expectedResult) { return newPlusResult }
            return newConcatResult
        }

        var newResult = testCalbration(expectedResult, currentValuePosition + 1, newMultResult, testValues)
        if (newResult == expectedResult) { return newResult }

        newResult = testCalbration(expectedResult, currentValuePosition + 1, newPlusResult, testValues)
        if (newResult == expectedResult) { return  newResult }

        newResult = testCalbration(expectedResult, currentValuePosition + 1, newConcatResult, testValues)
        if (newResult == expectedResult) { return  newResult }

        return 0
    }

    fun part2(input: List<String>): Long {
        var calibrations: Long = 0

        input.forEach { equation ->
            val parts = equation.split(":")
            val result = parts[0].trim().toLong()
            val testValues = parts[1].trim().split(" ").map { it.toInt() }

            val calibration = testCalbration(result, 1, testValues[0].toLong(), testValues)

            if (calibration == result) { calibrations += calibration }
        }

        return calibrations
    }

//    fun part2(input: List<String>): Int {
//    }

    val testInput = readInput("Day07_test")
//    println("Test output (part1): ${part1(testInput)}")
    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day07")
//    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
