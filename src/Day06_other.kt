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

    fun rotate(direction: String): String {
        return when (direction) {
            "^" -> ">"
            ">" -> "v"
            "v" -> "<"
            else -> "^"
        }
    }

    fun findClosestObstacle(direction: String, startX: Int, startY: Int, obstacleMap: Map<Int, List<Int>>): Pair<Int, Int> {
        var foundObstacle = Pair<Int, Int>(-1, -1)

        if ( direction == ">" ) {
            val newY = startY + 1
            val newX = obstacleMap[startY + 1]?.find { it > startX } ?: -1

            foundObstacle = newX to newY
        }

        if ( direction == "<" ) {
            val newY = startY - 1
            val newX = obstacleMap[startY - 1]?.reversed()?.find { it < startX } ?: -1

            foundObstacle = newX to newY
        }

        if ( direction == "^") {
            for (y in (startY - 1) downTo 0) {
                val newX = startX + 1
                if (obstacleMap[y]?.contains(newX) == true) {
                    foundObstacle = newX to y
                    break
                }
            }
        }

        if (direction == "v") {
            val maxY = obstacleMap.keys.maxOrNull() ?: 0
            for (y in (startY + 1)..maxY) {
                val newX = startX - 1
                if (obstacleMap[y]?.contains(startX - 1) == true) {
                    foundObstacle = newX to y
                    break
                }
            }
        }

        return foundObstacle
    }

    data class Obstacle(
        val x: Int,
        val y: Int,
        val directions: MutableSet<String>
    )

    fun isALoop(direction: String, x: Int, y: Int, obstacleMap: Map<Int, List<Int>>): Boolean {
        val map: MutableMap<Int, MutableList<Int>> = obstacleMap.mapValues { it.value.toMutableList() }.toMutableMap()

        map.compute(y) { _, list ->
            val updatedList = (list ?: mutableListOf())
            updatedList.add(x)
            updatedList.sorted().toMutableList()
        }

        var newDirection = rotate(direction)
        var posX = x
        var posY = y
        var newObstacle = -1 to -1

        val obstacleList: MutableList<Obstacle> = mutableListOf()
        obstacleList.add(Obstacle(x, y, mutableSetOf(direction)))

        while (true) {
            newObstacle = findClosestObstacle(newDirection, posX, posY, map)

            if (newObstacle.first == -1 || newObstacle.second == -1) { return false }
            val foundObstacle = obstacleList.find { it.x == newObstacle.first && it.y == newObstacle.second }

            if (foundObstacle != null) {
                if (foundObstacle.directions.contains(newDirection)) {
                    return true
                }

                foundObstacle.directions.add(newDirection)
            } else {
                obstacleList.add(Obstacle(newObstacle.first, newObstacle.second, mutableSetOf(newDirection)))
            }

            newDirection = rotate(newDirection)
            posX = newObstacle.first
            posY = newObstacle.second
        }
    }

    val guardDirections = mapOf(
        '^' to Pair(0, -1),
        'v' to Pair(0, 1),
        '>' to Pair(1, 0),
        '<' to Pair(-1, 0)
    )

    fun getCharFromDirection(direction: Pair<Int, Int>): Char {
        return guardDirections.entries.firstOrNull { it.value == direction }?.key ?: ' '
    }

    fun getDirection(char: Char): Pair<Int, Int> { return guardDirections[char] ?: Pair(0, 0) }

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

    fun markAsVisited(list: MutableList<String>, stringIndex: Int, charIndex: Int, direction: Pair<Int, Int>, isTurn: Boolean): Boolean {
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
        val obstacleMap = parseInput(input)
        val validObstacles: MutableList<Pair<Int, Int>> = mutableListOf()
        var validObstructions = 0

        val map = input.toMutableList()
        var guardPosition = getGuardPosition(map)
        var guardDirection = getDirection(map[guardPosition.second][guardPosition.first])
        var isInMap = true


        markAsVisited(map, guardPosition.second, guardPosition.first, guardDirection, false)

        while (isInMap) {
            val startY = guardPosition.second

            while (guardPosition.second > 0 && guardPosition.second < map.size - 1 && isInMap) {
                if (map[guardPosition.second + (1 * guardDirection.second)][guardPosition.first] == '#') {
                    guardDirection = (-1 * guardDirection.second) to 0
                    markAsVisited(map, guardPosition.second, guardPosition.first, guardDirection, true)
                    break
                } else {
                    val currentDir = getCharFromDirection(guardDirection)
                    var xx =  guardPosition.first
                    var yy = guardPosition.second + (1 * guardDirection.second)
                    val isValidObstacle = isALoop(currentDir.toString(), guardPosition.first, guardPosition.second + (1 * guardDirection.second), obstacleMap )

                    if (isValidObstacle) {
                        if (!validObstacles.contains(Pair(xx,yy))) {
                            validObstacles.add(Pair(xx, yy))
                            validObstructions++
                        }
                    }
                }

                guardPosition = guardPosition.first to (guardPosition.second + (1 * guardDirection.second))
                markAsVisited(map, guardPosition.second, guardPosition.first, guardDirection, false)
                if (guardPosition.second <= 0 || guardPosition.second >= map.size - 1) { isInMap = false; break }

                val startX = guardPosition.first
                while(guardPosition.first > 0 && guardPosition.first < map[0].length - 1) {

                    if (map[guardPosition.second][guardPosition.first + (1 * guardDirection.first)] == '#') {
                        guardDirection = 0 to guardDirection.first
                        markAsVisited(map, guardPosition.second, guardPosition.first, guardDirection, true)
                        break
                    } else {
                        val currentDir = getCharFromDirection(guardDirection)
                        val xx = guardPosition.first + (1 * guardDirection.first)
                        val yy = guardPosition.second
                        val isValidObstacle = isALoop(currentDir.toString(), guardPosition.first + (1 * guardDirection.first), guardPosition.second, obstacleMap )

                        if (isValidObstacle) {
                            if (!validObstacles.contains(Pair(xx,yy))) {
                                validObstacles.add(Pair(xx, yy))
                                validObstructions++
                            }
                        }
                    }

                    guardPosition = (guardPosition.first + (1 * guardDirection.first)) to guardPosition.second
                    markAsVisited(map, guardPosition.second, guardPosition.first, guardDirection, false)
                    if (guardPosition.first <= 0 || guardPosition.first >= map[0].length - 1) { isInMap = false; break }
                    if (startX == guardPosition.first) { break }
                }
                if (startY == guardPosition.second) { break }
            }
        }

        return validObstructions
    }


    val testInput = readInput("Day06_test")
    println("Test output (part): ${part1(testInput)}")

    val realInput = readInput("Day06")
    println("Real output (part): ${part1(realInput)}")

    val endTime = System.currentTimeMillis() // Record end time
    println("Execution time: ${endTime - startTime} ms")
}
