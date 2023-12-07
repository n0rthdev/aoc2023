import structure.LinesPuzzle

class Day07Part2 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {

        val hands = lines.map {
            Hand.fromString(it)
        }

        val sorted = hands.sorted()

        val ranked = sorted.mapIndexed { index, hand ->
            hand.bid * (index + 1)
        }

        return ranked.sum().toString()
    }

    companion object {
        enum class HandType {
            HIGH_CARD,
            ONE_PAIR,
            TWO_PAIR,
            THREE_OF_A_KIND,
            FULL_HOUSE,
            FOUR_OF_A_KIND,
            FIVE_OF_A_KIND
        }

        data class Hand(val handType: HandType, val value: Long, val card: String, val bid: Long) : Comparable<Hand> {

            override fun compareTo(other: Hand): Int {
                val type = this.handType.compareTo(other.handType)
                return if (type != 0) {
                    type
                } else {
                    this.value.compareTo(other.value)
                }
            }

            companion object {

                fun getValue(str: String): Long {
                    return str.toCharArray().map {
                        when (it) {
                            'A' -> 14
                            'K' -> 13
                            'Q' -> 12
                            'J' -> 1
                            'T' -> 10
                            else -> it.digitToInt()
                        }.toLong()
                    }.reduce { acc, i ->
                        acc * 15 + i
                    }
                }

                fun fromString(str: String): Hand {
                    val parts = str.split(" ")
                    return Hand(getType(parts[0]), getValue(parts[0]), parts[0], parts[1].toLong())
                }

                fun getType(cards: String): HandType {
                    val eachCount = cards.toCharArray().toList().groupingBy { it }.eachCount()
                    val jokerCount = eachCount.get('J') ?: 0
                    val noJokers = eachCount.toMutableMap()
                    if (jokerCount < 5) {
                        noJokers.remove('J')
                        val mostFoundCard = noJokers.maxBy { it.value }
                        noJokers[mostFoundCard.key] = jokerCount + mostFoundCard.value
                    }
                    check(noJokers.values.sum() == 5)
                    return when (noJokers.size) {
                        1 -> HandType.FIVE_OF_A_KIND
                        2 -> {
                            if (noJokers.containsValue(4)) {
                                HandType.FOUR_OF_A_KIND
                            } else {
                                HandType.FULL_HOUSE
                            }
                        }

                        3 -> {
                            if (noJokers.containsValue(3)) {
                                HandType.THREE_OF_A_KIND
                            } else {
                                HandType.TWO_PAIR
                            }
                        }

                        4 -> {
                            HandType.ONE_PAIR
                        }

                        5 -> {
                            HandType.HIGH_CARD
                        }

                        else -> {
                            throw RuntimeException("Invalid hand")
                        }
                    }
                }
            }


        }
    }
}