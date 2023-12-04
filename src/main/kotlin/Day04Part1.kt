import structure.LinesPuzzle

class Day04Part1 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val res = lines.map { line ->
            val nl = line.split(":")[1]
            val (win, my) = nl.split(" | ")
                .map {
                    it.split(" ")
                        .filter { it.trim().isNotBlank() }
                        .map { it.trim().toLong() }
                        .toSet()
                }

            val correctNumbers = my.intersect(win).size
            if (correctNumbers != 0) {
                Math.pow(2.0, correctNumbers - 1.0).toLong()
            } else {
                0L
            }
        }
        return res.sum().toString()
    }

    companion object {

    }
}