import kotlin.math.sign
import kotlin.math.abs

fun main() {
    val startTime = System.currentTimeMillis()

    fun checkReport(levels: List<Int>): Pair<String, Int> {
        val initialSign = (levels[0] - levels[1]).sign

        for (i in 0 until levels.size - 1) {
            val (current, next) = levels[i] to levels[i + 1]
            val diff = current - next
            when {
                abs(diff) !in 1..3 -> return Pair("Diff", i)
                diff.sign != initialSign -> return Pair("Sign", i)
            }
        }
        return Pair("Safe", -1)
    }

    fun part1(input: List<String>): Int {
        var safeReports = 0

        for (line in input) {
            val levels = line.split(" ").map { it.toInt() }

            val report = checkReport(levels)

            if (report.first == "Safe") { safeReports++ }
        }

        return safeReports
    }

    fun removeLevel(levels: List<Int>, indexToRemove: Int): List<Int> {
        return levels.take(indexToRemove) + levels.drop(indexToRemove + 1)
    }

    fun canFixReport(report: Pair<String, Int>, levels: List<Int> ): Boolean {
        if (report.first == "Sign" && (report.second == 0 || report.second == 1)) {
            if (checkReport(removeLevel(levels, 0)).first == "Safe") return true
            if (checkReport(removeLevel(levels, 1)).first == "Safe") return true
        } else {
            if (checkReport(removeLevel(levels, report.second + 1)).first == "Safe") return true
            if (checkReport(removeLevel(levels, report.second)).first == "Safe") return true
        }

        return false
    }

    fun part2(input: List<String>): Int {
        var safeReports = 0

        for (line in input) {
            val levels = line.split(" ").map { it.toInt() }

            val report = checkReport(levels)

            if (report.first != "Safe") {
                if (canFixReport(report, levels)) { safeReports++ }
            } else {
                safeReports++
            }
        }

        return safeReports
    }

    val testInput = readInput("Day02_test")
    println("Test output (part1): ${part1(testInput)}")
    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day02")
    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
