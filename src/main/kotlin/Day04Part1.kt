import structure.LinesPuzzle

class Day04Part1 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val res = lines.map { line ->
            val nl = line.split(":")[1]
            var (win, my) = nl.split(" | ")

            val winNr = win.trim().split(" ").filter { it.trim().isNotBlank() }.map { it.trim().toLong() }.toSet()
            val myNr = my.trim().split(" ").filter { it.trim().isNotBlank() }.map { it.trim().toLong() }.toSet()

            val correctNumbers = myNr.intersect(winNr).size
            if (correctNumbers != 0) {
                Math.pow(2.0, (myNr.intersect(winNr).size * 1.0) - 1.0).toLong()
            } else {
                0L
            }
        }
        return res.sum().toString()
    }

    companion object {

    }
}