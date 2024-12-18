import kotlin.math.pow

fun main() {
    val startTime = System.currentTimeMillis()

    fun parseBytes(input: List<String>, bytes: Int, memory: Int): MutableList<MutableList<Char>> {
        val computer: MutableList<MutableList<Char>> = MutableList(memory + 1) { MutableList(memory + 1) { '.' } }

        input.forEachIndexed { index, line ->
            if (index < bytes) {
                val (x, y) = line.split(",")
                computer[x.toInt()][y.toInt()] = '#'
            }
        }

        computer[memory][memory] = 'E'
        return computer
    }

    fun reachExit(computer: MutableList<MutableList<Char>>, pos: Pair<Int, Int>, memory: Int, steps: Int, shortestPath: MutableList<Int>): Unit {
        if (steps > shortestPath[0exi]) return
        if (pos.first > memory || pos.second > memory) return
        if (computer[pos.second][pos.first] == 'E') {
            if (steps < shortestPath[0]) shortestPath[0] = steps
            return
        }

        directions.forEach { direction ->
            val newPos = Pair(pos.first + direction.value.first, pos.second + direction.value.second)
            computer[pos.second][pos.first] = 'O'
            reachExit(computer, newPos, memory, steps + 1, shortestPath)
            computer[pos.second][pos.first] = '.'
        }
    }

    fun part1(input: List<String>): Int {
        val memory = 6
        val computer = parseBytes(input, 12, 6)
        val steps = 0
        val shortestPath: MutableList<Int> = mutableListOf(1)
        shortestPath[0] = Int.MAX_VALUE

        reachExit(computer, Pair(0,0), memory, steps, shortestPath)

        return steps
    }

//    fun part2(registers: MutableMap<Char, Int>, programs: List<Int>): Int {
//    }

    val testInput = readInput("Day17_test")
    println("Test output (part1): ${part1(testInput)}")
//    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day17")
//    println("Real output (part1): ${part1(realInput)}")
//    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
