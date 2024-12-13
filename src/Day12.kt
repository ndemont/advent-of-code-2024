fun main() {
    val startTime = System.currentTimeMillis()

    val directions = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))
    data class Region(val plantType: Char, var area: Int = 0, var perimeter: Int = 0)
    val regions: MutableList<Region> = mutableListOf()

    fun exploreRegion(farm: List<MutableList<Char>>, plant: Pair<Int, Int>, region: Region ): Unit {
        farm[plant.second][plant.first] = farm[plant.second][plant.first].lowercaseChar()
        region.area += 1

        directions.forEach { direction ->
            val nextX = plant.first + direction.first
            val nextY = plant.second + direction.second

            if (nextY in farm.indices && nextX in farm.first().indices) {
                if (farm[nextY][nextX] == region.plantType) exploreRegion(farm, Pair(nextX, nextY), region)
                else if (farm[nextY][nextX] != region.plantType.lowercaseChar()) region.perimeter++
//                else if (farm[nextY][nextX].isUpperCase()) region.perimeter++
            } else {
                region.perimeter++
            }
        }
    }

    fun registerRegion(x: Int, y: Int, regionPlant: Char, farm: List<MutableList<Char>>) {
        val newRegion = Region(plantType = regionPlant, area = 0, perimeter = 0)

        exploreRegion(farm, Pair(x, y), newRegion)

        regions.add(newRegion)
    }

    fun getSide(direction: Pair<Int, Int>): Pair<Int, Int> {
        return when (direction) {
            Pair(0, 1) -> Pair(1, 0)
            Pair(1, 0) -> Pair(0, -1)
            Pair(0, -1) -> Pair(-1, 0)
            else -> Pair(0, 1)
        }
    }

    fun isPlantType(plant: Char, plantType: Char): Boolean {
        return plant == plantType || plant == plantType.lowercaseChar()
    }

    fun exploreRegion2(farm: List<MutableList<Char>>, plant: Pair<Int, Int>, region: Region ): Unit {
        farm[plant.second][plant.first] = farm[plant.second][plant.first].lowercaseChar()
        region.area += 1

        directions.forEach { direction ->
            val y = plant.second + direction.second
            val x = plant.first + direction.first

            if (y in farm.indices && x in farm.first().indices) {
                val nextPlant = farm[y][x]

                if (nextPlant == region.plantType) exploreRegion2(farm, Pair(x, y), region)

                if (farm[y][x] != region.plantType.lowercaseChar()) {
                    val sideDirection = getSide(direction)
                    val sidePlant = Pair(plant.first + sideDirection.first, plant.second + sideDirection.second)

                    if (sidePlant.first !in farm.first().indices || sidePlant.second !in farm.indices) {
                        region.perimeter++
                    }
                    else if (isPlantType(farm[sidePlant.second][sidePlant.first], region.plantType)) {
                        val diagonalDirection = Pair(sideDirection.first + direction.first, sideDirection.second + direction.second)
                        val diagonalPlant = Pair(plant.first + diagonalDirection.first, plant.second + diagonalDirection.second)

                        if (diagonalPlant.second in farm.indices && diagonalPlant.first in farm.first().indices) {
                            if (isPlantType(farm[diagonalPlant.second][diagonalPlant.first], region.plantType)) {
                                region.perimeter++
                            }
                        }
                    }
                    else {
                        region.perimeter++
                    }
                }
            } else {
                val sideDirection = getSide(direction)
                val sidePlant = Pair(plant.first + sideDirection.first, plant.second + sideDirection.second)

                if (sidePlant.first !in farm.first().indices || sidePlant.second !in farm.indices) {
                    region.perimeter++
                }
                else if (!isPlantType(farm[sidePlant.second][sidePlant.first], region.plantType)) {
                    region.perimeter++
                }
            }
        }
    }

    fun registerRegion2(x: Int, y: Int, regionPlant: Char, farm: List<MutableList<Char>>) {
        val newRegion = Region(plantType = regionPlant, area = 0, perimeter = 0)

        exploreRegion2(farm, Pair(x, y), newRegion)

        regions.add(newRegion)
    }

//    fun part1(input: List<String>): Int {
//        val farm = input.map { str -> str.toMutableList() }
//
//        farm.forEach { println(it) }
//
//        farm.forEachIndexed { y, row ->
//            row.forEachIndexed { x, plant ->
//                if (plant.isUpperCase()) {
//                    registerRegion(x, y, plant, farm)
//                }
//            }
//        }
//
//        regions.forEach { println(it) }
//
//        return regions.sumOf { it.area * it.perimeter }
//    }

    fun part2(input: List<String>): Int {
        val farm = input.map { str -> str.toMutableList() }

        farm.forEach { println(it) }

        farm.forEachIndexed { y, row ->
            row.forEachIndexed { x, plant ->
                if (plant.isUpperCase()) {
                    registerRegion2(x, y, plant, farm)
                }
            }
        }

        regions.forEach { println(it) }

        return regions.sumOf { it.area * it.perimeter }
    }



    val testInput = readInput("Day12_test")
//    println("Test output (part1): ${part1(testInput)}")
//    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day12")
//    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
