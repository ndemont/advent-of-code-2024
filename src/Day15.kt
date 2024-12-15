fun main() {
    val startTime = System.currentTimeMillis()

    data class Cell(var pos: Pair<Int, Int>, val dir: Pair<Int, Int>, val prev: Pair<Int, Int>, val isSide: Boolean)

    fun findRobot(warehouse: List<MutableList<Char>>): Pair<Int, Int> {
        val rowIndex = warehouse.indexOfFirst { row -> '@' in row }
        val colIndex = warehouse[rowIndex].indexOf('@')
        warehouse[rowIndex][colIndex] = '.'

        return Pair(colIndex, rowIndex)
    }

    fun pushBoxes(robot: Pair<Int, Int>, startStep: Pair<Int, Int>, dir: Pair<Int, Int>, warehouse: List<MutableList<Char>>): Pair<Int, Int> {
        var nextItem = 'O'
        var nextStep = startStep
        val boxSteps: MutableList<Pair<Int, Int>> = mutableListOf()

        while (nextItem == 'O') {
            nextStep = nextStep.first + dir.first to nextStep.second + dir.second
            boxSteps.add(nextStep)
            nextItem = warehouse[nextStep.second][nextStep.first]
        }

        if (nextItem == '#') return robot

        warehouse[startStep.second][startStep.first] = '.'
        boxSteps.forEach { warehouse[it.second][it.first] = 'O' }

        return startStep
    }

    fun moveRobot(moves: String, warehouse: List<MutableList<Char>>) {
        var robot = findRobot(warehouse)

        moves.forEach { move ->
            val dir = directions[move]!!

            val nextStep = robot.first + dir.first to robot.second + dir.second
            val nextItem =  warehouse[nextStep.second][nextStep.first]

            if (nextItem == '.') robot = nextStep
            if (nextItem == 'O') robot = pushBoxes(robot, nextStep, dir, warehouse)
            println("Robot x: ${robot.first}, y: ${robot.second}")
            warehouse.forEach { it.println() }
            println()
        }
    }

    fun canMove(cell: Cell, warehouse: List<MutableList<Char>>, pushedBox: MutableMap<Pair<Int, Int>, Pair<Int, Int>>): Boolean {
        val item = warehouse[cell.pos.second][cell.pos.first]
        if (item == '#') return false
        if (item == '.') {
            pushedBox[cell.pos] = cell.prev
            return true
        }

        if (cell.dir.first == 0) {
            val nextCell = Cell(Pair(cell.pos.first, cell.pos.second + cell.dir.second), cell.dir, cell.pos, false )
            val nextCellItem = warehouse[nextCell.pos.second][nextCell.pos.first]
            val nextBox = if (cell.isSide && (item == nextCellItem)) {
                true
            } else {
                canMove(nextCell, warehouse, pushedBox)
            }

            val sideBox = if (cell.isSide) {
                true
            }   else if (item == ']') {
                val sideItem = cell.pos.first - 1 to cell.pos.second
                val prevItem = sideItem.first to sideItem.second - cell.dir.second
                val leftCell = Cell(sideItem, cell.dir, prevItem, true )
                canMove(leftCell, warehouse, pushedBox)
            } else {
                val sideItem = cell.pos.first + 1 to cell.pos.second
                val prevItem = sideItem.first to sideItem.second - cell.dir.second
                val rightCell = Cell(sideItem, cell.dir, prevItem, true )
                canMove(rightCell, warehouse, pushedBox)
            }

            val moved = nextBox && sideBox
            if (moved) pushedBox[cell.pos] = cell.prev
            return moved
        } else {
            val nextCell = Cell(Pair(cell.pos.first + cell.dir.first, cell.pos.second), cell.dir, cell.pos, false )
            val moved = canMove(nextCell, warehouse, pushedBox)
            if (moved) pushedBox[cell.pos] = cell.prev
            return moved
        }
    }

    fun moveRobot2(moves: String, warehouse: List<MutableList<Char>>) {
        var robot = findRobot(warehouse)

        moves.forEach { move ->
            val dir = directions[move]!!

            val nextStep = robot.first + dir.first to robot.second + dir.second
            val nextItem =  warehouse[nextStep.second][nextStep.first]

            if (nextItem == '.') robot = nextStep
            else if (nextItem == '[' || nextItem == ']') {
                val pushedBox: MutableMap<Pair<Int, Int>, Pair<Int, Int>> = mutableMapOf()

                val cell = Cell(nextStep, dir, robot, false)
                val moved = canMove(cell, warehouse, pushedBox)
                if (moved) {
                    robot = nextStep
                    pushedBox.forEach {
                        if (pushedBox[it.value] == null) { pushedBox[it.key] = Pair(-1, -1) }
                    }

                    pushedBox.forEach {
                        val newItem = if (it.value == Pair(-1, -1) ){
                            '.'
                            } else {
                            warehouse[it.value.second][it.value.first]
                        }

                        warehouse[it.key.second][it.key.first] = newItem
                    }
                }
            }

//            println("Robot x: ${robot.first}, y: ${robot.second}")
//            warehouse.forEachIndexed { indexY, _ ->
//                warehouse[indexY].forEachIndexed { indexX, _ ->
//                    if (robot.first == indexX && robot.second == indexY) {
//                        print('@')
//                    } else {
//                        print(warehouse[indexY][indexX])
//                    }
//                }
//                println()
//            }
//            println()
        }
    }

    fun boxesGPS(warehouse: List<List<Char>>): Int {
        return warehouse.indices.sumOf { row ->
            warehouse[row].indices.sumOf { column ->
                if (warehouse[row][column] == 'O' || warehouse[row][column] == '[') (100 * row) + column else 0
            }
        }
    }

    fun part1(input: List<String>): Int {
        val blank = input.indexOf("")
        val movements = input.subList(blank + 1, input.size).joinToString("")
        val warehouse = input.subList(0, blank).map { it.toMutableList() }.toMutableList()

        moveRobot(movements, warehouse)

        return boxesGPS(warehouse)
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
        moveRobot2(movements, warehouse)

        return boxesGPS(warehouse)
    }

    val testInput = readInput("Day15_test")
//    println("Test output (part1): ${part1(testInput)}")
//    println("Test output (part2): ${part2(testInput)}")

    val testInput2 = readInput("Day15_test2")
//    println("Test output 2 (part1): ${part1(testInput2)}")
//    println("Test output 2 (part2): ${part2(testInput2)}")

    val realInput = readInput("Day15")
//    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
