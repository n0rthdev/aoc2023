import structure.LinesPuzzle
import utils.Vector3D

class Day14Part2 : LinesPuzzle() {
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

        var cycleCount = 0L
        val cycleStarts = mutableMapOf<Set<Vector3D>,Long>()
        var movingRocks = roundRocks.toSet()
        var movedCycle = 0L
        do {
            if (cycleStarts.contains(movingRocks)){
                break
            }
            cycleStarts.put(movingRocks,cycleCount)
            movedCycle= 0L
            var pair = moveInDirection(movingRocks, height, width, fixedRocks, movedCycle, Vector3D.NORTH)
            movedCycle += pair.first
            movingRocks = pair.second
            pair = moveInDirection(movingRocks, height, width, fixedRocks, movedCycle, Vector3D.WEST)
            movedCycle += pair.first
            movingRocks = pair.second
            pair = moveInDirection(movingRocks, height, width, fixedRocks, movedCycle, Vector3D.SOUTH)
            movedCycle += pair.first
            movingRocks = pair.second
            pair = moveInDirection(movingRocks, height, width, fixedRocks, movedCycle, Vector3D.EAST)
            movedCycle += pair.first
            movingRocks = pair.second
            cycleCount++
        }while (true)

        val firstCycleStart = cycleStarts.getValue(movingRocks)
        val cycleLength = cycleCount - firstCycleStart
        val target = firstCycleStart + ((1000000000L - firstCycleStart) % cycleLength)

        val results = cycleStarts.filter { it.value == target }
        val resultRocks = results.keys.first()


        val weights = resultRocks.map { it.y + 1 }
        return weights.sum().toString()
    }

    private fun moveInDirection(
        movingRocks: Set<Vector3D>,
        height: Int,
        width: Int,
        fixedRocks: MutableSet<Vector3D>,
        movedCycle: Long,
        direction: Vector3D
    ): Pair<Long, Set<Vector3D>> {
        var movingRocks1 = movingRocks
        var movedCycle1 = movedCycle
        var movedDirection = 0L
        do {
            movedDirection = 0L
            movingRocks1 = movingRocks1.map {
                val newPos = it + direction
                if (newPos.y in 0..<height && newPos.x in 0..<width &&
                    !movingRocks1.contains(newPos) && !fixedRocks.contains(newPos)) {
                    movedDirection++
                    movedCycle1++
                    newPos
                } else {
                    it
                }
            }.toSet()
        } while (movedDirection > 0)
        return Pair(movedCycle1, movingRocks1)
    }

    companion object {

    }
}