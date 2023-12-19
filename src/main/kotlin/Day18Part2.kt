import structure.LinesPuzzle
import utils.Vector3D

class Day18Part2 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val instructions = lines.map { Instruction.parse(it) }

        val startPos = Vector3D.ZERO
        var currentPos = startPos
        val boarder = mutableMapOf<Vector3D, String>()
        var steps = 0

        val turns = mutableSetOf<Vector3D>()
        turns.add(currentPos)

        for (instruction in instructions) {
            currentPos = currentPos + instruction.totalMove
            turns.add(currentPos)
//            for (step in instruction.allSteps) {
//                currentPos += step
//                steps++
//                boarder[currentPos] = instruction.colorCode
//            }
        }
        turns.map { it.x }.toSet().sorted().withIndex().associate { it.index to it.value }
        turns.map { it.y }.toSet().sorted().withIndex().associate { it.index to it.value }

        check(startPos == currentPos)

        val x2 = boarder.keys.maxOf { it.x }
        val y2 = boarder.keys.maxOf { it.y }
        val x1 = boarder.keys.minOf { it.x }
        val y1 = boarder.keys.minOf { it.y }

        for (y in (y1 ..y2).reversed()){
            for (x in x1..x2){
                val pos = Vector3D(x,y,0)
                if(boarder.containsKey(pos)){
                    print("#")
                }
                else {
                    print(".")
                }
            }
            println()
        }
        println()

        val fill = mutableSetOf<Vector3D>()

        val corner1 = Vector3D(x1,y1,0)
        val corner2 = Vector3D(x2,y2,0)

        val outside =getoutside(x1, x2, y1, y2, boarder)

        val visited = mutableSetOf<Vector3D>()
        val toVisit = mutableSetOf<Vector3D>()

        toVisit.addAll(outside)

        while (toVisit.isNotEmpty()){
            val current = toVisit.first()
            toVisit.remove(current)
            visited.add(current)
            if(!boarder.containsKey(current)){
                fill.add(current)
                val neighbors = Vector3D.Directions2d.map { current + it }
                    .filter {
                    it.inCube(corner1, corner2)
                }
//                .filter { !boarder.containsKey(it) }
                    .filter { !visited.contains(it) }
                    .toSet()
                toVisit.addAll(neighbors)
            }
        }


        for (y in (y1 ..y2).reversed()){
            for (x in x1..x2){
                val pos = Vector3D(x,y,0)
                if(boarder.containsKey(pos)){
                    print("#")
                }
//                else if(visited.contains(pos)){
//                    print("X")
//                }
                else if(!fill.contains(pos)){
                    print("0")
                }
                else {
                  print(".")
                }
            }
            println()
        }
        val abs = (corner1 - corner2).abs()
        return (((abs.x+1) * (abs.y+1)) - fill.size).toString()
    }

    private fun getoutside(
        x1: Long,
        x2: Long,
        y1: Long,
        y2: Long,
        boarder: MutableMap<Vector3D, String>,
    ) : Set<Vector3D> {
        val outside = mutableSetOf<Vector3D>()
        for (x in setOf(x1, x2)) {
            for (y in y1..y2) {
                val pos = Vector3D(x, y, 0)
                if (!boarder.containsKey(pos)) {
                    outside.add(pos)
                }
            }
        }

        for (y in setOf(y1, y2)) {
            for (x in x1..x2) {
                val pos = Vector3D(x, y, 0)
                if (!boarder.containsKey(pos)) {
                    outside.add(pos)
                }
            }
        }
        return outside
    }

    companion object {
        data class Instruction(val dirCode: Char, val count: Long, val colorCode: String) {
            val dir = parser.parse(dirCode)
            val totalMove get() = dir * count
            val allSteps get() = (1..count).map { dir }

            companion object {
                val parser = Vector3D.Companion.DirectionParser.fromChars("0231XX")
                fun parse1(instruction: String): Instruction {
                    val parts = instruction.split(" ")
                    return Instruction(parts[0].first(), parts[1].toLong(), parts[2].trim('(', ')'))
                }
                fun parse(instruction: String): Instruction {
                    val parts = instruction.split(" ")
                    val color = parts[2].trim('(', ')')
                    val dist = Integer.decode(color.take(6).replace("#","0x"))
                    return Instruction(color[6], dist.toLong(), "")
                }
            }
        }
    }
}