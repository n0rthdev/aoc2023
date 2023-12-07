import structure.LinesPuzzle

class Day07Part1 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {

        val hands = lines.map {
            Hand.fromString(it)
        }

        val sorted = hands.sorted()

        val ranked = sorted.mapIndexed { index, hand ->
            hand.bid * (index+1)
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
                            'J' -> 11
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
                    return when (eachCount.size) {
                        1 -> HandType.FIVE_OF_A_KIND
                        2 -> {
                            if (eachCount.containsValue(4)) {
                                HandType.FOUR_OF_A_KIND
                            } else {
                                HandType.FULL_HOUSE
                            }
                        }

                        3 -> {
                            if (eachCount.containsValue(3)) {
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