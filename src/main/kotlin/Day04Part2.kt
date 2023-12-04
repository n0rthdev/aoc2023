import structure.LinesPuzzle

class Day04Part2 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val cards = lines.map { line ->
            val (cardName,nl) = line.split(":")
            val (win, my) = nl.split(" | ")
                .map {
                    it.split(" ")
                        .filter { it.trim().isNotBlank() }
                        .map { it.trim().toLong() }
                        .toSet()
                }

            Card(cardName.substringAfter("Card").trim().toLong(),
                win,
                my)
        }.associateBy { it.id }

        var myCards = cards.values
        var count = 0L
        while (myCards.isNotEmpty()) {
            val newCards = mutableListOf<Card>()
            for(mc in myCards) {
                count++
                mc.winningCards().forEach {
                    newCards.add(cards.getValue(it))
                }
            }
            myCards = newCards
        }

        return count.toString()
    }

    companion object {
        data class Card(val id: Long, val winningNr: Set<Long>, val myNr: Set<Long>) {
            fun winningCards(): Set<Long> {
                return myNr.intersect(winningNr).indices.map { id + 1 + it }.toSet()
            }
        }
    }
}