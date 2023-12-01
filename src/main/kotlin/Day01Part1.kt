import structure.LinesPuzzle

class Day01Part1 : LinesPuzzle() {


    companion object {

    }

    override fun solve(lines: List<String>): String {
        return lines.sumOf {
            val digits = it.toCharArray().filter {
                it >= '0' && it <= '9'
            }
            "${digits.first()}${digits.last()}".toLong()
        }.toString()
    }
}