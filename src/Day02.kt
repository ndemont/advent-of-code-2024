import kotlin.math.sign
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        var safeReports: Int = 0

        for (line in input) {
            val levels = line.split(" ")

            val level0 = levels[0].toInt()
            val level1 = levels[1].toInt()
            val sign = (level0 - level1).sign
            var reportUnsafe = false

            for (i in 0 until levels.size - 1) {
                val diff = levels[i].toInt() - levels[i + 1].toInt()

                if (abs(diff) < 1 || abs(diff) > 3) {
                    reportUnsafe = true
                    break
                }

                if (diff.sign != sign) {
                    reportUnsafe = true
                    break
                }
            }

            if (!reportUnsafe) {
                safeReports += 1
            }
        }

        return safeReports
    }

    fun isUnsafe(sign: Int, left: Int, right: Int): Boolean {
        val diff = left - right

        return abs(diff) !in 1..3 || diff.sign != sign
    }

    fun isSafe(sign: Int, left: Int, right: Int): Boolean {
        val diff = left - right

        return abs(diff) in 1..3 && diff.sign == sign
    }
    
    fun prevIsDampener(sign: Int, pos: Int, prev: Int, current: Int, next: Int, nextPlus: Int): Boolean {
        if (pos == 1) {
            val newSign = (current - next).sign
            val currentIsSafe = isSafe(newSign, current, next)
            val nextIsSafe = if (nextPlus == -1) true else isSafe(newSign, next, nextPlus)

            return currentIsSafe && nextIsSafe
        }

        return false
    }

    fun currentIsDampener(sign: Int, pos: Int, prev: Int, current: Int, next: Int, nextPlus: Int): Boolean {
        return when {
            pos < 1 -> {
                val newSign = (next - nextPlus).sign
                isSafe(newSign, next, nextPlus)
            }
            pos == 1 -> {
                val newSign = (prev - next).sign
                val prevIsSafe = isSafe(newSign, prev, next)
                val nextIsSafe = nextPlus == -1 || isSafe(newSign, next, nextPlus)
                prevIsSafe && nextIsSafe
            }
            else -> {
                val prevIsSafe = isSafe(sign, prev, next)
                val nextIsSafe = nextPlus == -1 || isSafe(sign, next, nextPlus)
                prevIsSafe && nextIsSafe
            }
        }
    }
    
    fun nextIsDampener(sign: Int, pos: Int, prev: Int, current: Int, next: Int, nextPlus: Int): Boolean {
        return when {
            nextPlus < 0 -> true
            pos == 0 -> {
                val newSign = (current - nextPlus).sign
                isSafe(newSign, current, nextPlus)
            }
            else -> isSafe(sign, current, nextPlus)
        }
    }
    
    fun isAProblemDampener(sign: Int, pos: Int, prev: Int, current: Int, next: Int, nextPlus: Int): Int {
        return when {
            nextIsDampener(sign, pos, prev, current, next, nextPlus) -> pos + 1
            prevIsDampener(sign, pos, prev, current, next, nextPlus) -> pos - 1
            currentIsDampener(sign, pos, prev, current, next, nextPlus) -> pos
            else -> -1
        }
    }

    fun part2(input: List<String>): Int {
        var safeReports: Int = 0

        for (line in input) {
            var problemDampenerPosition = -1
            val levels = line.split(" ")

            var sign = (levels[0].toInt() - levels[1].toInt()).sign
            var reportUnsafe = false

            for (i in 0 until levels.size - 1) {
                if (i == problemDampenerPosition) {
                    continue
                }

                if (isUnsafe(sign, levels[i].toInt(), levels[i + 1].toInt())) {
                    if (problemDampenerPosition == -1) {
                        val prev = if (i == 0) - 1 else levels[i - 1].toInt()
                        val current = levels[i].toInt()
                        val next = if (i > levels.size - 2) - 1 else levels[i + 1].toInt()
                        val nextPlus = if (i > levels.size - 3) - 1 else levels[i + 2].toInt()

                        problemDampenerPosition = isAProblemDampener(sign, i, prev, current, next, nextPlus)

                        if (problemDampenerPosition != -1) {
                            if (problemDampenerPosition in (0..1)) {
                                sign = (levels[1 - problemDampenerPosition].toInt() - levels[2].toInt()).sign
                            }
                            continue
                        }
                    }

                    reportUnsafe = true
                    break
                }
            }

            if (!reportUnsafe) {
                safeReports += 1
            }
        }

        return safeReports
    }

    // Run with test input
    val testInput = readInput("Day02_test")
    println("Test output: part1")
    part1(testInput).println()
    println("Test output: part2")
    part2(testInput).println()
    println()

    // Run with real input
    val input = readInput("Day02")
    println("Real output: part1")
    part1(input).println()
    println("Real output: part2")
    part2(input).println()
}
