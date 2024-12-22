fun main() {
    val startTime = System.currentTimeMillis()

    fun mix(value: Long, secret: Long): Long {
        return secret.xor(value)
    }

    fun prune(secret: Long): Long {
        return secret % 16777216
    }

    fun nextSecret(secret: Long): Long {
        val step1 = prune(mix(secret * 64, secret))
        val step2 = prune(mix(step1 / 32, step1))
        val step3 = prune(mix(step2 * 2048, step2))

        return step3
    }

    fun part1(input: List<String>): Long {
        val secrets = input.map { it.toLong()}
        var sum = 0L

        secrets.forEach {
            var secret = it
            repeat(2000) {
                secret = nextSecret(secret)
            }

//            println("$it: $secret")
            sum += secret
        }

        return sum
    }

    fun part2(input: List<String>, ): Int {
        val secrets = input.map { it.toLong()}
        val prices: MutableList<MutableMap<String, Int>> = mutableListOf()
        var mostBananas = 0

        secrets.forEach {
            var currentSecret = it.toLong()
            val sequence: MutableList<Int> = mutableListOf()
            val sequencesMap: MutableMap<String, Int> = mutableMapOf()

            repeat(2000) {
                val prevSecret = currentSecret
                currentSecret = nextSecret(currentSecret)

                val diff = (currentSecret % 10) - (prevSecret % 10)
                sequence.add(diff.toInt())

                if (sequence.size >= 4) {
                    if (sequence.size == 5) sequence.removeAt(0)

                    if (sequence.toString() !in sequencesMap) {
                        sequencesMap[sequence.toString()] = (currentSecret % 10).toInt()
                    }
                }
            }

            prices.add(sequencesMap)
        }

        prices.toList().forEach { buyer ->
            val buyerEntries = buyer.entries.toList()
            buyerEntries.forEach { sequence ->
                val priceVariation = sequence.key
                var currentBananas = 0

                prices.forEach { map ->
                    if (priceVariation in map) {
                        currentBananas += map[priceVariation]!!
                        map.remove(priceVariation)
                    }
                }

                if (currentBananas > mostBananas) {
                    mostBananas = currentBananas
                }
            }
        }

        return mostBananas
    }

    val testInput = readInput("Day22_test")
    println("Test output (part1): ${part1(testInput)}")
    println("Test output (part2): ${part2(testInput)}")

    val realInput = readInput("Day22")
    println("Real output (part1): ${part1(realInput)}")
    println("Real output (part2): ${part2(realInput)}")

    val endTime = System.currentTimeMillis()
    println("Execution time: ${endTime - startTime} ms")
}
