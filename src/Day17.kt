import kotlin.math.pow

fun main() {
    val startTime = System.currentTimeMillis()

    fun comboOperands(registers: MutableMap<Char, Int>, operand: Int): Int {
        val comboOperands = mapOf(
            0 to 0,
            1 to 1,
            2 to 2,
            3 to 3,
            4 to (registers['A']),
            5 to (registers['B']),
            6 to (registers['C']),
            7 to 0
        )

        return comboOperands[operand]!!
    }

    fun executeInstruction(registers: MutableMap<Char, Int>, opcode: Int, operand: Int, instruction: Int, output: MutableList<Int>): Int {
        var nextInstruction = instruction + 2

        when (opcode) {
            0 -> { registers['A'] = (registers['A']!! / 2.0.pow(comboOperands(registers, operand))).toInt() }
            1 -> { registers['B'] = registers['B']!!.xor(operand) }
            2 -> { registers['B'] = comboOperands(registers, operand) % 8 }
            3 -> { if (registers['A'] != 0) nextInstruction = operand }
            4 -> { registers['B'] = registers['B']!!.xor(registers['C']!!) }
            5 -> { output.add(comboOperands(registers, operand) % 8)}
            6 -> { registers['B'] = (registers['A']!! / 2.0.pow(comboOperands(registers, operand))).toInt() }
            7 -> { registers['C'] = (registers['A']!! / 2.0.pow(comboOperands(registers, operand))).toInt() }
        }

        return nextInstruction
    }

    fun parseInstruction(input: List<String>): Pair<MutableMap<Char, Int>, List<Int>> {
        val registers = mutableMapOf<Char, Int>()
        var program: List<Int> = emptyList()

        input.forEach { line ->
            when {
                line.startsWith("Register") -> {
                    val parts = line.split(": ")
                    val registerName = parts[0].removePrefix("Register ").trim().first() // Extract the char, e.g., 'A'
                    val registerValue = parts[1].toIntOrNull() ?: 0
                    registers[registerName] = registerValue
                }
                line.startsWith("Program:") -> {
                    val programData = line.removePrefix("Program: ").split(",").map { it.trim().toInt() }
                    program = programData
                }
            }
        }

        return registers to program
    }

    fun part1(registers: MutableMap<Char, Int>, programs: List<Int>): String {
        val output: MutableList<Int> = mutableListOf()

        var instruction = 0
        while (instruction < programs.size - 1) {
            val opcode = programs[instruction]
            val operand = programs[instruction + 1]
            instruction = executeInstruction(registers, opcode, operand, instruction, output)
        }

        return output.joinToString(",")
    }

    fun part2(registers: MutableMap<Char, Int>, programs: List<Int>): Int {
        var registerA = 0
        val wrongCopies: MutableMap<Triple<Int, Int, Int>, Int>  = mutableMapOf()

        while (true) {
            println(registerA)
            var oldWrongCopy = false
            registers['A'] = registerA
            val output: MutableList<Int> = mutableListOf()

            var instruction = 0
            while (instruction < programs.size - 1) {
                val currentRegisters = Triple(registers['A']!!, registers['B']!!, registers['C']!!)
                if (wrongCopies[currentRegisters] != null) {
                    if (wrongCopies[currentRegisters] == instruction) {
                        oldWrongCopy = true
                        break
                    }
                }

                val opcode = programs[instruction]
                val operand = programs[instruction + 1]
                instruction = executeInstruction(registers, opcode, operand, instruction, output)

                if (!(output.zip(programs).all { (a, b) -> a == b }) ) break
            }

            if (output == programs) return registerA
            if (!oldWrongCopy) wrongCopies[Triple(registers['A']!!, registers['B']!!, registers['C']!!)] =  instruction

            registerA++
        }
    }

    val testInput = readInput("Day17_test")
    val (registers, programs) = parseInstruction(testInput)
//    println("Test output (part1): ${part1(registers, programs)}")
    println("Test output (part2): ${part2(registers, programs)}")

    val realInput = readInput("Day17")
    val (registers2, programs2) = parseInstruction(realInput)
//    println("Real output (part1): ${part1(registers2, programs2)}")
    println("Real output (part2): ${part2(registers2, programs2)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
