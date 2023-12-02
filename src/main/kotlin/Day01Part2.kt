import structure.LinesPuzzle

class Day01Part2 : LinesPuzzle() {


    companion object {

    }

    override fun solve(lines: List<String>): String {

        return lines.sumOf { line ->
            val newline = line.replace("one","one1one").
            replace("two","two2two").
            replace("three","three3three").
            replace("four","four4four").
            replace("five","five5five").
            replace("six","six6six").
            replace("seven","seven7seven").
            replace("eight","eight8eight").
            replace("nine","nine9nine")
            "${newline.first { it.isDigit() }}${newline.last { it.isDigit() }}".toLong()
        }.toString()
    }

    fun solveLong(lines: List<String>): String {

        val translationMap = mapOf(
            "one" to "1",
            "two" to "2",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to "6",
            "seven" to "7",
            "eight" to "8",
            "nine" to "9",
        )

        return lines.sumOf { line ->

            val numbers = mutableListOf<Int>()

            for (i in line.indices) {
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