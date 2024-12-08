fun main() {
    val startTime = System.currentTimeMillis()

    fun parseAntennas(input: List<String>): MutableMap<Char, MutableList<Pair<Int, Int>>> {
        val antennas = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char != '.') {
                    antennas.getOrPut(char) { mutableListOf() }.add(Pair(x, y))
                }
            }
        }

        return antennas
    }

    fun addAntinode(cityMap: List<MutableList<Char>>, stringIndex: Int, charIndex: Int): Int {
        return if (cityMap[stringIndex][charIndex] != '#') {
            cityMap[stringIndex][charIndex] = '#'
            1
        } else {
            0
        }
    }

    fun part1(input: List<String>): Int {
        val cityMap = input.map { it.toMutableList() }
        val antennas = parseAntennas(input)
        var antinodes = 0

        antennas.forEach { (_, positions) ->
            for (i in positions.indices) {
                val pair1 = positions[i]

                for (j in i + 1 until positions.size) {
                    val pair2 = positions[j]

                    val symmetric = Pair(2 * pair2.first - pair1.first, 2 * pair2.second - pair1.second)

                    if (symmetric.second in cityMap.indices && symmetric.first in cityMap[0].indices) {
                        antinodes += addAntinode(cityMap, symmetric.second, symmetric.first)
                    }
                }
            }
        }

        return antinodes
    }

    fun axisOfSymmetry(pair1: Pair<Int, Int>, pair2: Pair<Int, Int>, range: IntRange): List<Pair<Int, Int>> {
        val deltaX = pair2.first - pair1.first
        val deltaY = pair2.second - pair1.second

        return range.mapNotNull { x ->
            val dx = x - pair1.first

            if (dx % deltaX == 0) {
                val y = pair1.second + (dx * deltaY / deltaX)
                Pair(x, y)
            } else {
                null
            }
        }
    }

    fun part2(input: List<String>): Int {
        val cityMap = input.map { it.toMutableList() }
        val antennas = parseAntennas(input)
        var antinodes = 0

        antennas.forEach { (_, positions) ->
            for (i in positions.indices) {
                val pair1 = positions[i]

                for (j in i + 1 until positions.size) {
                    val pair2 = positions[j]

                    val axisPoints = axisOfSymmetry(pair1, pair2, cityMap.indices)

                    axisPoints.filter { it.second in cityMap.indices && it.first in cityMap[0].indices }
                        .forEach { point -> antinodes += addAntinode(cityMap, point.second, point.first) }
                }
            }
        }

        return antinodes
    }

    val testInput = readInput("Day08_test")
    println("Test output (part1): ${part1(testInput)}")
    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day08")
    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
