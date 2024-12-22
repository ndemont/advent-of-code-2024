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
    var bestPaths: MutableSet<Pair<Int, Int>> = mutableSetOf()

    fun exitMaze(maze: List<MutableList<Char>>, reindeer: Reindeer, score: MutableList<Int>, path: MutableList<Pair<Int, Int>>): Unit {
        val nextStep = maze[reindeer.move.second][reindeer.move.first]

        val currentScore = scores[reindeer.move]
        if (currentScore != null && reindeer.score > currentScore) return

        if (nextStep == '#' || nextStep in directions.keys) return
        if (nextStep == 'E') {
            if (reindeer.score == score[0]) {
                bestPaths.addAll(path.toMutableSet())
            } else if (reindeer.score < score[0]){
                score[0] = reindeer.score
                bestPaths = path.toMutableSet()
            }

            return
        }

        val steps: List<Char> = listOf(reindeer.move.third, rotateLeft(reindeer.move.third), rotateRight(reindeer.move.third))
        steps.forEach { nextDirection ->
            val nextScore = if (nextDirection == reindeer.move.third) { reindeer.score + 1 } else { reindeer.score + 1001 }

            if (nextScore <= score[0]) {
                val nextMove = Triple(
                    reindeer.move.first + directions[nextDirection]!!.first,
                    reindeer.move.second + directions[nextDirection]!!.second,
                    nextDirection
                )

                val nextReindeer = Reindeer(nextMove, nextScore)


                maze[reindeer.move.second][reindeer.move.first] = nextDirection
                path.add(Pair(reindeer.move.first, reindeer.move.second))
                exitMaze(maze, nextReindeer, score, path)
                path.removeLast()
                maze[reindeer.move.second][reindeer.move.first] = '.'
            }
        }

        if (currentScore == null || reindeer.score < currentScore) { scores[reindeer.move] = reindeer.score }
        return
    }

    fun part1(input: List<String>): Int {
        val maze = input.map { it.toMutableList() }.toMutableList()
        val winningScores: MutableList<Int> = mutableListOf(Int.MAX_VALUE)
        val path: MutableList<Pair<Int, Int>> = mutableListOf()
        val startReindeer = Reindeer(Triple(1, maze.size -2, '>'), 0)

        exitMaze(maze, startReindeer, winningScores, path)

        println(winningScores)
        return bestPaths.size + 1
    }

    val testInput = readInput("Day16_test")
//    println("Test output (part1): ${part1(testInput)} == 45 7036")

    val testInput2 = readInput("Day16_test2")
    println("Test output (part1): ${part1(testInput2)} == 64")


    val realInput = readInput("Day16")
//    println("Real output (part1): ${part1(realInput)} == 609")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}