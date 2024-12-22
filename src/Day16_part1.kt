fun main() {
    val startTime = System.currentTimeMillis()

    data class Reindeer(var move: Triple<Int, Int, Char>, val score: Int)

    fun rotateRight(direction: Char): Char {
        return when (direction) {
            '>' -> 'v'
            'v' -> '<'
            '<' -> '^'
            else -> '>'
        }
    }

    fun rotateLeft(direction: Char): Char{
        return when (direction) {
            '>' -> '^'
            '^' -> '<'
            '<' -> 'v'
            else -> '>'
        }
    }

    val scores: MutableMap<Triple<Int, Int, Char>, Int> = mutableMapOf()

    fun exitMaze(maze: List<MutableList<Char>>, reindeer: Reindeer, score: MutableList<Int>): Unit {
        val nextStep = maze[reindeer.move.second][reindeer.move.first]

        val currentScore = scores[reindeer.move]
        if (currentScore != null && reindeer.score > currentScore) return

        if (nextStep == '#' || nextStep in directions.keys) return
        if (nextStep == 'E' && reindeer.score < score[0]) {
            maze.forEach { println(it) }
            println(reindeer)
            println()
            score[0] = reindeer.score ; return
        }

        val steps: List<Char> = listOf(reindeer.move.third, rotateLeft(reindeer.move.third), rotateRight(reindeer.move.third))
        steps.forEach { nextDirection ->
            val nextScore = if (nextDirection == reindeer.move.third) { reindeer.score + 1 } else { reindeer.score + 1001 }

            if (nextScore < score[0]) {
                val nextMove = Triple(
                    reindeer.move.first + directions[nextDirection]!!.first,
                    reindeer.move.second + directions[nextDirection]!!.second,
                    nextDirection
                )

                val nextReindeer = Reindeer(nextMove, nextScore)

                maze[reindeer.move.second][reindeer.move.first] = nextDirection
                exitMaze(maze, nextReindeer, score)
                maze[reindeer.move.second][reindeer.move.first] = '.'
            }
        }

        if (currentScore == null || reindeer.score < currentScore) { scores[reindeer.move] = reindeer.score }
        return
    }

    fun part1(input: List<String>): Int {
        val maze = input.map { it.toMutableList() }.toMutableList()
        val winningScores: MutableList<Int> = mutableListOf(Int.MAX_VALUE)
        val startReindeer = Reindeer(Triple(1, maze.size -2, '>'), 0)

        exitMaze(maze, startReindeer, winningScores)

        return winningScores[0]
    }

    fun part2(input: List<String>): Int {
        return 1
    }

    val testInput = readInput("Day16_test")
//    println("Test output (part1): ${part1(testInput)} - expected 7036")
//    println("Test output (part2): ${part2(testInput)}")

    val testInput2 = readInput("Day16_test2")
//    println("Test output (part1): ${part1(testInput2)} - expected 11048")
//    println("Test output (part2): ${part2(testInput)}")


    val realInput = readInput("Day16")
//    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
