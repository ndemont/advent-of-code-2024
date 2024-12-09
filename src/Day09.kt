fun main() {
    val startTime = System.currentTimeMillis()

    fun moveFiles(filesToMove: MutableList<Char>, filesInFront: MutableList<Char>): Pair<MutableList<Char>, MutableList<Char>> {
        return filesInFront to filesToMove
    }

    fun part1(input: List<String>): Int {
        val diskMap: MutableList<String> = mutableListOf()
        var countinit = 0


        input.first().forEachIndexed { index, blocks ->
            if (index % 2 == 0) {
                val newFile = (index / 2).toString().repeat(blocks.toString().toInt())
                countinit += (blocks.toString().toInt() * (index / 2).toString().length)
                diskMap.add(newFile)
            } else {
                val newFile = '.'.toString().repeat(blocks.toString().toInt())
                diskMap.add(newFile)
            }
        }

        val diskMapChar: MutableList<Char> = diskMap.flatMap { it.toList() }.toMutableList()
        var files = diskMapChar.reversed()
        var dotIndex = 0
        var countref = 0
        var countresult = 0

        files.forEachIndexed() { index, file ->
            if (file != '.') {
                countref++
                while (dotIndex < diskMapChar.size && diskMapChar[dotIndex] != '.') {
                    dotIndex++
                }

                if (dotIndex < files.size - 1 - index) {
                    diskMapChar[dotIndex] = file
                    diskMapChar[files.size - 1 - index] = '.'
                    dotIndex++
                    println("last dot index: $dotIndex, last char: $file, last char index: ${files.size -1 - index}")
                }
            }
        }

        println(diskMap)
        println(diskMapChar)
        var sum = 0
        diskMapChar.forEachIndexed() { index, char ->
            if (char != '.')  {
                countresult++
                sum += (char.toString().toInt() * index)
            }
        }

        println("$countref == $countresult == $countinit")

        return sum
    }

//    fun part2(input: List<String>): Int {
//
//    }

    val testInput = readInput("Day09_test")
    println("Test output (part1): ${part1(testInput)}")
//    println("Test output (part2): ${part2(testInput)}")
//
    val realInput = readInput("Day09")
//    println("Real output (part1): ${part1(realInput)}")
//    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
