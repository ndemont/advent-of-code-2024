fun main() {
    fun xmasCount(line: String): Int {
        println(line)
        println("XMAS = ${Regex("XMAS").findAll(line).count()}")
        println("SAMX = ${Regex("SAMX").findAll(line).count()}")
        println("")

        return Regex("XMAS").findAll(line).count() + Regex("SAMX").findAll(line).count()
    }

    fun getDiagonal(input: List<String>, xPos: Int, yUp: Int, yDown: Int): Pair<String, String> {
        var diagonalUp = ""
        var diagonalDown = ""
        var yUpPos = yUp
        var yDownPos = yDown

        for (x in xPos until input[0].length) {
            if (yDownPos < input.size) {
                diagonalDown += input[yDownPos][x]
                yDownPos++
            }

            if (yUpPos >= 0) {
                diagonalUp += input[yUpPos][x]
                yUpPos--
            }
        }

        return diagonalUp to diagonalDown
    }

    fun part1(input: List<String>): Int {
        var xmasCount: Int = 0

        for ((index, line) in input.withIndex()) {
            println("Y = $index")
            val (diagonalUp, diagonalDown) = getDiagonal(input, 0, index, index)
            println("diagonalUp")
            xmasCount += xmasCount(diagonalUp)
            println("diagonalDown")
            xmasCount += xmasCount(diagonalDown)
            println("horizontal")
            xmasCount += xmasCount(line)
        }

        for (i in input[0].indices)  {
            val horizontalLine = input.map { it[i] }.joinToString("")
            println("X = $i")
            println("vertical")
            xmasCount += xmasCount(horizontalLine)

            if (i > 0) {
                val (diagonalUp, diagonalDown) = getDiagonal(input, i, input.size - 1, 0)

                println("diagonalUp")
                xmasCount += xmasCount(diagonalUp)
                println("diagonalDown")
                xmasCount += xmasCount(diagonalDown)
            }
        }

        return xmasCount
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day04_test")
    println("Test output (part1): ${part1(testInput)}")
//    println("Test output (part2): ${part2(testInput)}")
//
    val realInput = readInput("Day04")
    println("Real output (part1): ${part1(realInput)}")
//    println("Real output (part2): ${part2(realInput)}")
}
