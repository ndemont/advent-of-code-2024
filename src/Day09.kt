fun main() {
    val startTime = System.currentTimeMillis()

    fun part1(input: List<String>): Long {
        val diskMap: MutableList<String> = mutableListOf()

        input.first().forEachIndexed { index, blocks ->
            val newFile = if (index % 2 == 0) (index / 2).toString() else "."
            repeat(blocks.toString().toInt()) { diskMap.add(newFile) }
        }

        var dotIndex = 0
        diskMap.reversed().forEachIndexed { reverseIndex, file ->
            if (file != ".") {
                while (dotIndex < diskMap.size && diskMap[dotIndex] != ".") { dotIndex++ }

                if (dotIndex < diskMap.size - 1 - reverseIndex) {
                    diskMap[dotIndex] = file
                    diskMap[diskMap.size - 1 - reverseIndex] = "."
                    dotIndex++
                }
            }
        }

        return diskMap.mapIndexedNotNull { index, file ->
            file.toIntOrNull()?.times(index)
        }.sumOf { it.toLong() }
    }

    fun part2(input: List<String>): Long {
        val diskMap: MutableList<MutableList<String>> = mutableListOf()

        input.first().forEachIndexed { index, blocks ->
            val newFile = if (index % 2 == 0) (index / 2).toString() else "."
            val newFileList = MutableList(blocks.toString().toInt()) { newFile }
            if (newFileList.isNotEmpty()) {
                diskMap.add(newFileList)
            }
        }

        for (reverseIndex in diskMap.size - 1 downTo 0) {
            val files = diskMap[reverseIndex]
            if (files.first() != ".") {
                val availableSpotIndex = diskMap.indexOfFirst { block -> block.contains(".") && block.count { it == "." } >= files.size }

                if (availableSpotIndex != -1 && availableSpotIndex < reverseIndex) {
                    val startEmptyIndex = diskMap[availableSpotIndex].indexOfFirst { it == "." }

                    files.forEachIndexed { i, file ->
                        diskMap[availableSpotIndex][startEmptyIndex + i] = file
                        diskMap[reverseIndex][i] = "."
                    }
                }
            }
        }

        return diskMap.flatten().mapIndexedNotNull { index, file ->
            file.toIntOrNull()?.times(index)
        }.sumOf { it.toLong() }
    }

    val testInput = readInput("Day09_test")
    println("Test output (part1): ${part1(testInput)}")
    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day09")
    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
