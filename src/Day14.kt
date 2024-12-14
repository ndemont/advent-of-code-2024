import java.io.File

fun main() {
    val startTime = System.currentTimeMillis()

    data class Robot(var p: Pair<Int, Int>, val v: Pair<Int, Int>)

    fun parseRobots(input: List<String>): MutableList<Robot> {
        val regex = """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".toRegex()
        return input.map { line ->
            val (px, py, vx, vy) = regex.matchEntire(line)!!.destructured
            Robot(Pair(px.toInt(), py.toInt()), Pair(vx.toInt(), vy.toInt()))
        }.toMutableList()
    }

    fun moveRobot(robot: Robot, seconds: Int, mapSize: Pair<Int, Int>) : Pair<Int, Int>{
        var pX = (robot.p.first + (robot.v.first * seconds)) % mapSize.first
        var pY = (robot.p.second + (robot.v.second * seconds)) % mapSize.second

        if (pX < 0) pX += mapSize.first
        if (pY < 0) pY += mapSize.second

        return pX to pY
    }

    fun safetyFactor(robots: List<Robot>, mapSize: Pair<Int, Int>): Int {
        val quadrantCounts = IntArray(4) { 0 }
        val halfX = mapSize.first / 2
        val halfY = mapSize.second / 2

        robots.forEach { robot ->
            val (x, y) = robot.p
            val quadrant = when {
                x < halfX && y < halfY -> 0
                x > halfX && y < halfY -> 1
                x < halfX && y > halfY -> 2
                x > halfX && y > halfY -> 3
                else -> null
            }

            if (quadrant != null) quadrantCounts[quadrant]++
        }

        return quadrantCounts.reduce { safetyFactor, quadrantCount -> safetyFactor * quadrantCount }
    }

    fun part1(input: List<String>, mapSize: Pair<Int, Int>): Int {
        val robots = parseRobots(input)

        for (index in robots.indices) { robots[index].p = moveRobot(robots[index], 100, mapSize) }

        return safetyFactor(robots, mapSize)
    }

    fun saveLobbyMapToFile(lobbyMap: List<MutableList<String>>, index: Int) {
        val fileName = "output/Day14/grid_$index.txt"
        val file = File(fileName)

        file.printWriter().use { writer ->
            lobbyMap.forEach { line ->
                writer.println(line.joinToString(" "))
            }
        }
    }

    fun part2(input: List<String>, mapSize: Pair<Int, Int>): Unit {
        val robots = parseRobots(input)

        repeat(10000) {
            val lobbyMap = List(mapSize.second) { MutableList(mapSize.first) { " " } }

            for (index in robots.indices) {
                robots[index].p = moveRobot(robots[index], 1, mapSize)
                lobbyMap[robots[index].p.second][robots[index].p.first] = "X"
            }

            saveLobbyMapToFile(lobbyMap, it)
        }
    }

    val testInput = readInput("Day14_test")
    println("Test output (part1): ${part1(testInput, Pair(11, 7))}")
    println("Test output (part2): ${part2(testInput, Pair(11, 7))}")

    val realInput = readInput("Day14")
    println("Real output (part1): ${part1(realInput, Pair(101, 103))}")
    println("Real output (part2): ${part2(realInput, Pair(101, 103))}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
