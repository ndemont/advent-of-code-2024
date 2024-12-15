fun main() {
    val startTime = System.currentTimeMillis()

    data class Cell(var pos: Pair<Int, Int>, val dir: Pair<Int, Int>)

    fun findRobot(warehouse: List<MutableList<Char>>): Pair<Int, Int> {
        val rowIndex = warehouse.indexOfFirst { row -> '@' in row }
        val colIndex = warehouse[rowIndex].indexOf('@')
        warehouse[rowIndex][colIndex] = '.'

        return Pair(colIndex, rowIndex)
    }

    fun canMove(cell: Cell, warehouse: List<MutableList<Char>>, isSide: Boolean): Boolean {
        val item = warehouse[cell.pos.second][cell.pos.first]
        if (item == '#') return false
        if (item == '.') { return true }

        if (cell.dir.first == 0) {
            val nextCell = Cell(Pair(cell.pos.first, cell.pos.second + cell.dir.second), cell.dir )
            val nextBox = canMove(nextCell, warehouse, false)

            val sideBox = if (isSide) {
                true
            } else if (item == ']') {
                val sideItem = cell.pos.first - 1 to cell.pos.second
                val leftCell = Cell(sideItem, cell.dir)
                canMove(leftCell, warehouse, true)
            } else {
                val sideItem = cell.pos.first + 1 to cell.pos.second
                val rightCell = Cell(sideItem, cell.dir)
                canMove(rightCell, warehouse, true)
            }

            return nextBox && sideBox
        } else {
            val nextCell = Cell(Pair(cell.pos.first + cell.dir.first, cell.pos.second), cell.dir)
            return canMove(nextCell, warehouse, false)
        }
    }

    fun moveBoxes(cell: Cell, warehouse: List<MutableList<Char>>, newItem: Char, isSide: Boolean): Unit {
        val item = warehouse[cell.pos.second][cell.pos.first]
        if (isSide) {
            warehouse[cell.pos.second][cell.pos.first] = '.'
        } else {
            warehouse[cell.pos.second][cell.pos.first] = newItem
        }
        if (item == '.') { return }

        if (cell.dir.first == 0) {
            val nextCell = Cell(Pair(cell.pos.first, cell.pos.second + cell.dir.second), cell.dir )
            moveBoxes(nextCell, warehouse, item, false)

            if (!isSide && item == ']') {
                val sideItem = cell.pos.first - 1 to cell.pos.second
                val leftCell = Cell(sideItem, cell.dir)
                val oldItem = warehouse[sideItem.second - cell.dir.second][sideItem.first]

                moveBoxes(leftCell, warehouse, oldItem, true)
            } else if (!isSide && item == '['){
                val sideItem = cell.pos.first + 1 to cell.pos.second
                val rightCell = Cell(sideItem, cell.dir)
                val oldItem = warehouse[sideItem.second - cell.dir.second][sideItem.first]

                moveBoxes(rightCell, warehouse, oldItem, true)
            }
        } else {
            val nextCell = Cell(Pair(cell.pos.first + cell.dir.first, cell.pos.second), cell.dir)
            moveBoxes(nextCell, warehouse, item, false)
        }
    }

    fun moveRobot(moves: String, warehouse: List<MutableList<Char>>) {
        var robot = findRobot(warehouse)

        moves.forEach { move ->
            val dir = directions[move]!!

            val nextStep = robot.first + dir.first to robot.second + dir.second
            val nextItem =  warehouse[nextStep.second][nextStep.first]

            if (nextItem == '.') robot = nextStep
            else if (nextItem == '[' || nextItem == ']') {
                val cell = Cell(nextStep, dir)
                if (canMove(cell, warehouse, false)) {
                    moveBoxes(cell, warehouse, '.', false)
                    robot = nextStep
                }
            }

            println("Robot x: ${robot.first}, y: ${robot.second}")
            warehouse.forEachIndexed { indexY, _ ->
                warehouse[indexY].forEachIndexed { indexX, _ ->
                    if (robot.first == indexX && robot.second == indexY) {
                        print('@')
                    } else {
                        print(warehouse[indexY][indexX])
                    }
                }
                println()
            }
            println()
        }
    }

    fun boxesGPS(warehouse: List<List<Char>>): Int {
        return warehouse.indices.sumOf { row ->
            warehouse[row].indices.sumOf { column ->
                if (warehouse[row][column] == 'O' || warehouse[row][column] == '[') (100 * row) + column else 0
            }
        }
    }

    fun parseWarehouse(warehouse: List<String>): List<MutableList<Char>> {
        val resizedWarehouse: MutableList<MutableList<Char>> = mutableListOf()

        warehouse.forEach {row ->
            val newRow: MutableList<Char> = mutableListOf()

            row.forEach {cell ->
                when (cell) {
                    'O' -> {
                        newRow.add('[')
                        newRow.add(']')
                    }
                    '@' -> {
                        newRow.add('@')
                        newRow.add('.')
                    }
                    else -> {
                        repeat(2) { newRow.add(cell) }
                    }
                }
            }
            resizedWarehouse.add(newRow)
        }
        return resizedWarehouse
    }
    
    fun part2(input: List<String>): Int {
        val blankLineIndex = input.indexOf("")
        val movements = input.subList(blankLineIndex + 1, input.size).joinToString("")
        val warehouse = parseWarehouse(input.subList(0, blankLineIndex))

        warehouse.forEach { it.println() }
        moveRobot(movements, warehouse)

        return boxesGPS(warehouse)
    }

    val testInput = readInput("Day15_test")
    println("Test output (part2): ${part2(testInput)}")

    val testInput2 = readInput("Day15_test2")
    println("Test output 2 (part2): ${part2(testInput2)}")

    val realInput = readInput("Day15")
    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
