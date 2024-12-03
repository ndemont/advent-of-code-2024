import java.io.File

fun main() {
    val mulRegex = Regex("""mul\((\d+),(\d+)\)""")
    val dontRegex = Regex("""don't\(\)""")
    val doRegex = Regex("""do\(\)""")
    val allRegex = Regex("""mul\((\d+),(\d+)\)|don't\(\)|do\(\)""")

    fun multiply(regex: String): Int {
        val (x, y) = mulRegex.matchEntire(regex)!!.destructured

        return x.toInt() * y.toInt()
    }

    fun part1(input: List<String>): Int {
        var sum = 0

        for (line in input) {
            val matches = mulRegex.findAll(line)

            for (match in matches) { sum += multiply(match.value) }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        var isValid = true

        for (line in input) {
            val matches = allRegex.findAll(line)

            for (match in matches) {
                val regex = match.value

                when {
                    dontRegex.matches(regex) -> { isValid = false }
                    doRegex.matches(regex) -> { isValid = true }
                    mulRegex.matches(regex) && isValid -> { sum += multiply(regex) }
                }
            }
        }

        return sum
    }

    // Run with test input
    val testInput = readInput("Day03_test")
    println("Test output: part1")
    part1(testInput).println()
    println("Test output: part2")
    part2(testInput).println()

    // Run with real input
    val input = readInput("Day03")
    println("Real output: part1")
    part1(input).println()
    println("Real output: part2")
    part2(input).println()
}
