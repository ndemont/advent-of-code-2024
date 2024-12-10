fun main() {
    val startTime = System.currentTimeMillis()

    fun countTrailheads(row: Int, column: Int, next: Int, map: List<MutableList<Int>>): Int {
        if (row !in map.indices || column !in 0 until map[0].size) return 0
        if (map[row][column] != next) return 0

        if (next == 9) {
            return if (map[row][column] == 9) {
//                map[row][column] = -1
                1
            }
            else 0
        }

        var count = 0

        count += countTrailheads(row, column + 1, next + 1,  map )
        count += countTrailheads(row + 1, column , next + 1, map )
        count += countTrailheads(row - 1, column , next + 1, map )
        count += countTrailheads(row, column - 1 , next + 1, map )

        return count
    }

    fun part1(input: List<String>): Int {
        var trailheadsCount = 0
        val map = input.map { line ->
            line.split("")
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
        }

        map.forEach{println(it)}

        for (rowIndex in map.indices) {
            for (colIndex in map[rowIndex].indices) {
                if (map[rowIndex][colIndex] == 0) {
                    val copyMap = map.map { it.toMutableList() }
                    trailheadsCount += countTrailheads(rowIndex, colIndex, 0, copyMap)
                }
            }
        }


        return trailheadsCount
    }

//    fun part2(input: List<String>): Long {
//
//    }

    val testInput = readInput("Day10_test")
    println("Test output (part1): ${part1(testInput)}")
//    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day10")
    println("Real output (part1): ${part1(realInput)}")
//    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
