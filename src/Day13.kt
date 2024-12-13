fun main() {
    val startTime = System.currentTimeMillis()

    data class Game(val buttonA: Pair<Long, Long>, val buttonB: Pair<Long, Long>, val prize: Pair<Long, Long>)

    fun parseGames(input: List<String>): List<Game> {
        return input.chunked(4).mapNotNull { chunk ->
            val buttonARegex = Regex("""X\+(\d+), Y\+(\d+)""")
            val prizeRegex = Regex("""X=(\d+), Y=(\d+)""")

            val buttonA = buttonARegex.find(chunk[0])?.destructured
                ?.let { (x, y) -> Pair(x.toLong(), y.toLong()) }
            val buttonB = buttonARegex.find(chunk[1])?.destructured
                ?.let { (x, y) -> Pair(x.toLong(), y.toLong()) }
            val prize = prizeRegex.find(chunk[2])?.destructured
                ?.let { (x, y) -> Pair(x.toLong(), y.toLong()) }

            if (buttonA != null && buttonB != null && prize != null) {
                Game(buttonA, buttonB, prize)
            } else null
        }
    }

    fun findCheapestWin(game: Game): Pair<Long, Long> {
        var pushA = 0.toLong()

        while (true) {
            if ( pushA > game.prize.second) return Pair(-1, -1)
            val numerator = (game.prize.first - (game.buttonA.first * pushA))

            if (numerator % game.buttonB.first != 0.toLong()) {
                pushA++
                continue
            }
            val pushB = numerator / game.buttonB.first

            val prizeAttempt = (game.buttonA.second * pushA) + (game.buttonB.second * pushB)
            if (prizeAttempt == game.prize.second) return Pair(pushA, pushB)

            pushA++
        }
    }

    fun part1(input: List<String>): Long {
        val games = parseGames(input)
        var costToWin = 0.toLong()

        games.forEach { game ->
            val (pushA, pushB) = findCheapestWin(game)
            if (pushA >= 0) {
//                println(game)
                costToWin += (pushA * 3) + pushB
            }
        }
        return costToWin
    }

    fun part2(input: List<String>): Long {
        val games = parseGames(input)
        var costToWin = 0.toLong()

        games.forEach { game ->
            val newPrize = Pair(game.prize.first + 10000000000000, game.prize.second + 10000000000000)
            val currentGame = Game(game.buttonA, game.buttonB, newPrize)

            val (pushA, pushB) = findCheapestWin(currentGame)
            if (pushA >= 0) {
                println(currentGame)
                costToWin += (pushA * 3) + pushB
            }
        }
        return costToWin
    }

    val testInput = readInput("Day13_test")
    println("Test output (part1): ${part1(testInput)}")
    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day13")
    println("Real output (part1): ${part1(realInput)}")
//    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
