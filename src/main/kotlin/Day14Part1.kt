import structure.LinesPuzzle
import utils.Vector3D

class Day14Part1 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val height = lines.size
        val width = lines.first().length

        val roundRocks = mutableSetOf<Vector3D>()
        val fixedRocks = mutableSetOf<Vector3D>()
        for (li in lines.indices) {
            for (x in lines[li].indices) {
                val pos = Vector3D(x, height - 1 - li, 0)
                if (lines[li][x] == 'O') {
                    roundRocks.add(pos)
                } else if (lines[li][x] == '#') {
                    fixedRocks.add(pos)
                }
            }
        }

        var moved = 0L
        var movingRocks = roundRocks.toSet()
        do {
            moved = 0L
            movingRocks = movingRocks.map {
                val newPos = it + Vector3D.NORTH
                if (newPos.y >=0 && newPos.y < height && newPos.x >= 0 && newPos.x < width && !movingRocks.contains(newPos) && !fixedRocks.contains(newPos)) {
                    moved++
                    newPos
                } else {
                    it
                }
            }.toSet()
        } while (moved > 0)

        val weights = movingRocks.map { it.y + 1 }
        return weights.sum().toString()
    }

    companion object {

    }
}