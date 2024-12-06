fun main() {
    val startTime = System.currentTimeMillis()
    val guardDirections = mapOf(
        '^' to Pair(0, -1),
        'v' to Pair(0, 1),
        '>' to Pair(1, 0),
        '<' to Pair(-1, 0)
    )

    fun getDirection(char: Char): Pair<Int, Int> { return guardDirections[char] ?: Pair(0, 0) }

    fun getCharFromDirection(direction: Pair<Int, Int>): Char {
        return guardDirections.entries.firstOrNull { it.value == direction }?.key ?: ' '
    }

    fun getGuardPosition(map: List<String>): Pair<Int, Int> {
        var guardPosition: Pair<Int, Int> = -1 to -1

        map.forEachIndexed { y, line ->
            val x = line.indexOfFirst { char -> char in guardDirections.keys }

            if (x != -1) {
                guardPosition = x to y
                return guardPosition
            }
        }

        return guardPosition
    }

    fun markAsVisited(list: MutableList<String>, stringIndex: Int, charIndex: Int): Int {
        if (list[stringIndex][charIndex] == 'x') { return 0 }

        val currentString = list[stringIndex]
        val modifiedString = StringBuilder(currentString)
        modifiedString[charIndex] = 'x'
        list[stringIndex] = modifiedString.toString()
        return 1
    }

    fun addObstacle(list: MutableList<String>, stringIndex: Int, charIndex: Int) {
        val currentString = list[stringIndex]
        val modifiedString = StringBuilder(currentString)
        modifiedString[charIndex] = '#'
        list[stringIndex] = modifiedString.toString()
    }

    fun markAsVisited2(list: MutableList<String>, stringIndex: Int, charIndex: Int, direction: Pair<Int, Int>, isTurn: Boolean): Boolean {
        list.forEach { it -> println(it) }
        println()
        val currentChar = list[stringIndex][charIndex]
        if (getDirection(currentChar) == direction) { return true }
        if ((currentChar in guardDirections) && isTurn) { return false}

        val currentString = list[stringIndex]
        val modifiedString = StringBuilder(currentString)
        modifiedString[charIndex] = getCharFromDirection(direction)
        list[stringIndex] = modifiedString.toString()
        return false
    }

    fun part1(input: List<String>): Int {
        var totalPos = 0
        val map = input.toMutableList()
        var guardPosition = getGuardPosition(map)
        var guardDirection = getDirection(map[guardPosition.second][guardPosition.first])
        var isInMap = true


        totalPos += markAsVisited(map, guardPosition.second, guardPosition.first)

        while (isInMap) {
            val startY = guardPosition.second

            while (guardPosition.second > 0 && guardPosition.second < map.size - 1 && isInMap) {

                if (map[guardPosition.second + (1 * guardDirection.second)][guardPosition.first] == '#') {
                    guardDirection = (-1 * guardDirection.second) to 0
                    totalPos += markAsVisited(map, guardPosition.second, guardPosition.first)
                    break
                }

                guardPosition = guardPosition.first to (guardPosition.second + (1 * guardDirection.second))
                totalPos += markAsVisited(map, guardPosition.second, guardPosition.first)
                if (guardPosition.second <= 0 || guardPosition.second >= map.size - 1) { isInMap = false; break }

                val startX = guardPosition.first
                while(guardPosition.first > 0 && guardPosition.first < map[0].length - 1) {

                    if (map[guardPosition.second][guardPosition.first + (1 * guardDirection.first)] == '#') {
                        guardDirection = 0 to guardDirection.first
                        totalPos += markAsVisited(map, guardPosition.second, guardPosition.first)
                        break
                    }

                    guardPosition = (guardPosition.first + (1 * guardDirection.first)) to guardPosition.second
                    totalPos += markAsVisited(map, guardPosition.second, guardPosition.first)
                    if (guardPosition.first <= 0 || guardPosition.first >= map[0].length - 1) { isInMap = false; break }
                    if (startX == guardPosition.first) { break }
                }
                if (startY == guardPosition.second) { break }
            }
        }

        return totalPos
    }

    fun part2(input: List<String>): Int {
        var validObstructions = 0
        val initialMap = input.toMutableList()

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char !in guardDirections && char != '#') {
                    val map = input.toMutableList()

                    addObstacle(map,y,x)

                    var guardPosition = getGuardPosition(initialMap)
                    var guardDirection = getDirection(initialMap[guardPosition.second][guardPosition.first])

                    var isInMap = true
                    var isInLoop = false
                    while (isInMap && !isInLoop) {
                        val startY = guardPosition.second

                        while (guardPosition.second > 0 && guardPosition.second < map.size - 1 && isInMap && !isInLoop) {
                            if (map[guardPosition.second + (1 * guardDirection.second)][guardPosition.first] == '#') {
                                guardDirection = (-1 * guardDirection.second) to 0
                                isInLoop = markAsVisited2(map, guardPosition.second, guardPosition.first, guardDirection, true)
                                break
                            }

                            guardPosition = guardPosition.first to (guardPosition.second + (1 * guardDirection.second))
                            if (guardPosition.second <= 0 || guardPosition.second >= map.size - 1) { isInMap = false; break }

                            val startX = guardPosition.first
                            while(guardPosition.first > 0 && guardPosition.first < map[0].length - 1 && !isInLoop) {

                                if (map[guardPosition.second][guardPosition.first + (1 * guardDirection.first)] == '#') {
                                    guardDirection = 0 to guardDirection.first
                                    isInLoop = markAsVisited2(map, guardPosition.second, guardPosition.first, guardDirection, true)
                                    break
                                }

                                guardPosition = (guardPosition.first + (1 * guardDirection.first)) to guardPosition.second
                                if (guardPosition.first <= 0 || guardPosition.first >= map[0].length - 1) { isInMap = false; break }
                                if (startX == guardPosition.first) { break }
                                isInLoop = markAsVisited2(map, guardPosition.second, guardPosition.first, guardDirection, false)
                            }

                            if (startY == guardPosition.second) { break }
                            isInLoop = markAsVisited2(map, guardPosition.second, guardPosition.first, guardDirection, false)
                        }
                    }

                    if (isInLoop) { validObstructions++}
                }
            }
        }

        return validObstructions
    }

    val testInput = readInput("Day06_test")
    println("Test output (part1): ${part1(testInput)}")
    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day06")
    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
