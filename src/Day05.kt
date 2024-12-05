fun main() {
    fun parseInput(input: List<String>): Pair<MutableMap<Int, MutableList<Int>>, MutableList<List<Int>>> {
        val pageOrderingRules: MutableMap<Int, MutableList<Int>> = mutableMapOf()
        val pageUpdates: MutableList<List<Int>> = mutableListOf()

        for (line in input) {
            if (line.length == 5) {
                val (key, value) = line.split('|').map { it.toInt() }
                pageOrderingRules.getOrPut(key) { mutableListOf() }.add(value)
            }
            if (line.length > 5) {
                pageUpdates.add(line.split(',').map { it.toInt() })
            }
        }

        return Pair(pageOrderingRules, pageUpdates)
    }

    fun part1(input: List<String>): Int {
        var middlePageNumberSum = 0
        val (pageOrderingRules, pageUpdates) = parseInput(input)

        for (update in pageUpdates) {
            var ruleSetIsInvalid = false

            for (i in update.indices) {
                val currentRule = update[i]

                for (j in i + 1 until update.size) {
                    val checkRule = update[j]

                    if (pageOrderingRules[checkRule]?.contains(currentRule) == true) {
                        ruleSetIsInvalid = true
                    }
                }
            }

            if (!ruleSetIsInvalid) {
                middlePageNumberSum += update[update.size / 2]
            }
        }

        return middlePageNumberSum
    }

    fun sortKeys(map: MutableMap<Int, MutableList<Int>>, order: List<Int>): List<Int> {
        val sortedKeys = mutableListOf<Int>()

        for (rule in order) {
            var keyAdded = false

            for ((index, existingKey) in sortedKeys.withIndex()) {
                if (map[rule]?.contains(existingKey) == true) {
                    sortedKeys.add(index, rule)
                    keyAdded = true
                    break
                }
            }

            if (!keyAdded) {
                sortedKeys.add(rule)
            }
        }

        return sortedKeys
    }

    fun part2(input: List<String>): Int {
        var middlePageNumberSum = 0
        val (pageOrderingRules, pageUpdates) = parseInput(input)

        pageUpdates.forEach { update ->
            val fixedOrder = sortKeys(pageOrderingRules, update)

            if (update != fixedOrder) {
                middlePageNumberSum += fixedOrder[fixedOrder.size / 2]
            }
        }

        return middlePageNumberSum
    }


    val testInput = readInput("Day05_test")
    println("Test output (part1): ${part1(testInput)}")
    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day05")
    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")
}
