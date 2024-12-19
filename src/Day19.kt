fun main() {
    val startTime = System.currentTimeMillis()

    fun parseOnsen(input: List<String>): Pair<HashSet<String>, List<String>> {
        val patterns = input.first().split(",").map { it.trim() }.toHashSet()
        val designs = input.drop(2)

        return Pair(patterns, designs)
    }

    fun findOneArrangement(design: String, patterns: HashSet<String>, wrongPatterns: HashSet<String>): Int {
        if (design.isEmpty()) return 1

        for (split in 1..design.length) {
            val prefix = design.substring(0, split)
            val suffix = design.substring(split)

            if (prefix in patterns) {
                if (findOneArrangement(suffix, patterns, wrongPatterns) == 1) {
                    patterns.add(suffix)
                    return 1
                }
            }
        }

        return 0
    }

    fun part1(input: List<String>): Int {
        val (patterns, designs) = parseOnsen(input)
        val wrongPatterns: HashSet<String> = hashSetOf()
        var validDesigns = 0

        designs.forEach { design ->
            validDesigns += findOneArrangement(design, patterns, wrongPatterns)
        }

        return validDesigns
    }

    fun findUniqArrangements(design: String, cachedPatterns: MutableMap<String, Long>, patterns: HashSet<String>): Long {
        if (design.isEmpty()) return 1.toLong()
        if (design in cachedPatterns) return cachedPatterns[design]!!

        var count = 0.toLong()
        for (pattern in patterns) {
            if (design.startsWith(pattern)) {
                count = (count + findUniqArrangements(design.substring(pattern.length), cachedPatterns, patterns))
            }
        }

        cachedPatterns[design] = count
        return count
    }

    fun part2(input: List<String>): Long {
        val (patterns, designs) = parseOnsen(input)
        var validArrangements: Long = 0

        designs.forEach { design ->
            val cachedPatterns = mutableMapOf<String, Long>()
            validArrangements += findUniqArrangements(design, cachedPatterns, patterns)
        }

        return validArrangements
    }

    val testInput = readInput("Day19_test")
    println("Test output (part1): ${part1(testInput)}")
    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day19")
    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
