fun main() {
    val startTime = System.currentTimeMillis()

    data class Game(val buttonA: Pair<Double, Double>, val buttonB: Pair<Double, Double>, val prize: Pair<Double, Double>)

    fun parseGames(input: List<String>): List<Game> {
        return input.chunked(4).mapNotNull { chunk ->
            val buttonARegex = Regex("""X\+(\d+), Y\+(\d+)""")
            val prizeRegex = Regex("""X=(\d+), Y=(\d+)""")

            val buttonA = buttonARegex.find(chunk[0])?.destructured
                ?.let { (x, y) -> Pair(x.toDouble(), y.toDouble()) }
            val buttonB = buttonARegex.find(chunk[1])?.destructured
                ?.let { (x, y) -> Pair(x.toDouble(), y.toDouble()) }
            val prize = prizeRegex.find(chunk[2])?.destructured
                ?.let { (x, y) -> Pair(x.toDouble(), y.toDouble()) }

            if (buttonA != null && buttonB != null && prize != null) {
                Game(buttonA, buttonB, prize)
            } else null
        }
    }

    fun findCheapestWin(game: Game): Pair<Double, Double> {
        val numerator = (game.buttonB.first * game.prize.second) - (game.buttonB.second * game.prize.first)
        val denominator = (game.buttonA.second * game.buttonB.first) - (game.buttonB.second * game.buttonA.first)
        val a = numerator / denominator
        val b = (game.prize.first - (a * game.buttonA.first )) / game.buttonB.first

        if (a == a.toLong().toDouble() && b == b.toLong().toDouble()) return a to b
        return -1.0 to -1.0
    }

    fun part1(input: List<String>): Long {
        val games = parseGames(input)
        var costToWin = 0.0

        games.forEach { game ->
            val (pushA, pushB) = findCheapestWin(game)
            if (pushA >= 0) {
                costToWin += (pushA * 3) + pushB
            }
        }
        return costToWin.toLong()
    }

    fun part2(input: List<String>): Long {
        val games = parseGames(input)
        var costToWin = 0.0

        games.forEach { game ->
            val newPrize = Pair(game.prize.first + 10000000000000, game.prize.second + 10000000000000)
            val currentGame = Game(game.buttonA, game.buttonB, newPrize)

            val (pushA, pushB) = findCheapestWin(currentGame)
            if (pushA >= 0) {
                costToWin += (pushA * 3) + pushB
            }
        }
        return costToWin.toLong()
    }

    val testInput = readInput("Day13_test")
    println("Test output (part1): ${part1(testInput)}")
    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day13")
    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
