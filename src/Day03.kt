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
        return input
            .flatMap { line -> mulRegex.findAll(line) }
            .sumOf { match -> multiply(match.value) }
    }

    fun part2(input: List<String>): Int {
        var isEnabled = true

        return input.flatMap { line ->
            allRegex.findAll(line).map { match ->
                val regex = match.value
                when {
                    dontRegex.matches(regex) -> { isEnabled = false; 0 }
                    doRegex.matches(regex) -> { isEnabled = true; 0 }
                    mulRegex.matches(regex) && isEnabled -> multiply(regex)
                    else -> 0
                }
            }
        }.sum()
    }

    val testInput = readInput("Day03_test")
    println("Test output (part1): ${part1(testInput)}")
    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day03")
    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")
}
