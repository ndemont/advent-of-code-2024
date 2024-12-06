import javax.swing.text.Position

class NextObstacles(
    val up: Pair<Int, Int>,
    val down: Pair<Int, Int>,
    val left: Pair<Int, Int>,
    val right: Pair<Int, Int>
)

fun main() {
    val startTime = System.currentTimeMillis()

    fun parseInput(input: List<String>): Map<Int, List<Int>> {
        return input
            .mapIndexedNotNull { y, line ->
                val xPositions = line
                    .mapIndexedNotNull { x, char -> if (char == '#') x else null }

                if (xPositions.isNotEmpty()) y to xPositions else null
            }
            .toMap()
    }
//
//    fun buildNextObstacleMap(obstacleMap: Map<Int, List<Int>>):  MutableMap<Pair<Int, Int>, NextObstacles> {
//        val obstaclesMap: MutableMap<Pair<Int, Int>, NextObstacles> = mutableMapOf()
//
//
//    }

    val guardDirections = mapOf(
        '^' to Pair(0, -1),
        'v' to Pair(0, 1),
        '>' to Pair(1, 0),
        '<' to Pair(-1, 0)
    )

    fun getDirection(char: Char): Pair<Int, Int> { return guardDirections[char] ?: Pair(0, 0) }

    fun getCharFromDirection(direction: Pair<Int, Int>): Char {
        return guardDirections.entries.firstOrNull { it.value == direction }?.key ?: ' ' // Return a default char if not found
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

    fun addObstacle(list: MutableList<String>, stringIndex: Int, charIndex: Int) {
        val currentString = list[stringIndex]
        val modifiedString = StringBuilder(currentString)
        modifiedString[charIndex] = '#'
        list[stringIndex] = modifiedString.toString()
    }

    fun markAsVisited2(list: MutableList<String>, stringIndex: Int, charIndex: Int, direction: Pair<Int, Int>, isTurn: Boolean): Boolean {
        val currentChar = list[stringIndex][charIndex]
        if (getDirection(currentChar) == direction) { return true }
        if ((currentChar in guardDirections) && isTurn) { return false}

        val currentString = list[stringIndex]
        val modifiedString = StringBuilder(currentString)
        modifiedString[charIndex] = getCharFromDirection(direction)
        list[stringIndex] = modifiedString.toString()
        return false
    }

//    fun part2(input: List<String>): Int {
//        val obstaclesMap = parseInput(input)
//        val nextObstaclesMap = buildNextObstacleMap(obstaclesMap)
//
//
//
//        var validObstructions = 0
//        val initialMap = input.toMutableList()
//
//        input.forEachIndexed { y, line ->
//            line.forEachIndexed { x, char ->
//                if (char !in guardDirections && char != '#') {
//                    val map = input.toMutableList()
//
//                    addObstacle(map,y,x)
//
//                    var guardPosition = getGuardPosition(initialMap)
//                    var guardDirection = getDirection(initialMap[guardPosition.second][guardPosition.first])
//
//                    var isInMap = true
//                    var isInLoop = false
//                    while (isInMap && !isInLoop) {
//                        val startY = guardPosition.second
//
//                        while (guardPosition.second > 0 && guardPosition.second < map.size - 1 && isInMap && !isInLoop) {
//                            if (map[guardPosition.second + (1 * guardDirection.second)][guardPosition.first] == '#') {
//                                guardDirection = (-1 * guardDirection.second) to 0
//                                isInLoop = markAsVisited2(map, guardPosition.second, guardPosition.first, guardDirection, true)
//                                break
//                            }
//
//                            guardPosition = guardPosition.first to (guardPosition.second + (1 * guardDirection.second))
//                            if (guardPosition.second <= 0 || guardPosition.second >= map.size - 1) { isInMap = false; break }
//
//                            val startX = guardPosition.first
//                            while(guardPosition.first > 0 && guardPosition.first < map[0].length - 1 && !isInLoop) {
//
//                                if (map[guardPosition.second][guardPosition.first + (1 * guardDirection.first)] == '#') {
//                                    guardDirection = 0 to guardDirection.first
//                                    isInLoop = markAsVisited2(map, guardPosition.second, guardPosition.first, guardDirection, true)
//                                    break
//                                }
//
//                                guardPosition = (guardPosition.first + (1 * guardDirection.first)) to guardPosition.second
//                                if (guardPosition.first <= 0 || guardPosition.first >= map[0].length - 1) { isInMap = false; break }
//                                if (startX == guardPosition.first) { break }
//                                isInLoop = markAsVisited2(map, guardPosition.second, guardPosition.first, guardDirection, false)
//                            }
//
//                            if (startY == guardPosition.second) { break }
//                            isInLoop = markAsVisited2(map, guardPosition.second, guardPosition.first, guardDirection, false)
//                        }
//                    }
//
//                    if (isInLoop) { validObstructions++}
//                }
//            }
//        }
//
//        return validObstructions
//    }
//
//    val testInput = readInput("Day06_test")
//    println("Test parser: ${parseInput(testInput)}")
//    println("Test output (part2): ${part2(testInput)}")
//
//    val realInput = readInput("Day06")
////    println("Real output (part2): ${part2(realInput)}")
//
//    val endTime = System.currentTimeMillis() // Record end time
//    println("Execution time: ${endTime - startTime} ms")
}
