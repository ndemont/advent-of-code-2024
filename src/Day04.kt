fun main() {
    fun xmasCount(line: String): Int {
        return Regex("XMAS").findAll(line).count() + Regex("SAMX").findAll(line).count()
    }

    fun getDiagonals(input: List<String>, xPos: Int, yUp: Int, yDown: Int): Pair<String, String> {
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
            val (diagonalUp, diagonalDown) = getDiagonals(input, 0, index, index)
            xmasCount += xmasCount(diagonalUp)
            xmasCount += xmasCount(diagonalDown)
            xmasCount += xmasCount(line)
        }

        for (i in input[0].indices)  {
            val horizontalLine = input.map { it[i] }.joinToString("")
            xmasCount += xmasCount(horizontalLine)
            if (i > 0) {
                val (diagonalUp, diagonalDown) = getDiagonals(input, i, input.size - 1, 0)
                xmasCount += xmasCount(diagonalUp)
                xmasCount += xmasCount(diagonalDown)
            }
        }
        return xmasCount
    }


    fun isXmas(input: List<String>, x: Int, y: Int) : Int {
        if (x == 0 || y == 0) {
            return 0
        }

        if ((x >= input[0].length -1 ) || (y >= input.size - 1)) {
            return 0
        }

        val diagonalUp = buildString {
            append(input[y + 1][x - 1])
            append(input[y][x])
            append(input[y - 1][x + 1])
        }

        val diagonalDown = buildString {
            append(input[y - 1][x - 1])
            append(input[y][x])
            append(input[y + 1][x + 1])
        }

        if ((diagonalUp == "MAS" || diagonalUp == "SAM") && (diagonalDown == "MAS" || diagonalDown == "SAM")) {
            return 1
        }

        return 0
    }

    fun part2(input: List<String>): Int {
        var xmasCount = 0

        for ((y, line) in input.withIndex()) {
            for (x in line.indices) {
                if (line[x] == 'A') {
                    xmasCount += isXmas(input, x, y)
                }
            }
        }

        return xmasCount
    }

    val testInput = readInput("Day04_test")
    println("Test output (part1): ${part1(testInput)}")
    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day04")
    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")
}
