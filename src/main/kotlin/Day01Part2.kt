import structure.LinesPuzzle
import java.util.stream.IntStream.range

class Day01Part2 : LinesPuzzle() {


    companion object {

    }

    override fun solve(lines: List<String>): String {

        val translationMap = mapOf(
            "two" to "2",
            "one" to "1",
            "eight" to "8",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to "6",
            "nine" to "9",
            "seven" to "7"
        )


        return lines.sumOf { line ->

            val numbers = mutableListOf<Int>()

            for (i in range(0, line.length)) {
                if (line[i].isDigit()) {
                    numbers.add(line[i].code - '0'.code)
                } else {
                    val parsedNumber = translationMap.keys.firstOrNull { line.substring(i).startsWith(it) }
                    if (parsedNumber != null) {
                        numbers.add(translationMap.getValue(parsedNumber).toInt())
                    }
                }
            }
            numbers.first() * 10 + numbers.last()
        }.toString()
    }
}