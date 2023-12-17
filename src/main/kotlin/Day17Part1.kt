import structure.LinesPuzzle
import utils.AStar
import utils.Vector3D

class Day17Part1 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val height = lines.size
        val width = lines[0].length

        val tiles = mutableMapOf<Vector3D,Int>()

        for (li in lines.indices){
            val y = height - li -1
            for (x in lines[li].indices){
                val tile = lines[li][x].digitToInt()
                tiles[Vector3D(x,y,0)] = tile
            }
        }

        val start = Vector3D(0,height-1,0)
        val target = Vector3D(width-1,0,0)

        val state = State(start,target, tiles, emptyList())

        return AStar<CostVector>().findPath(state).cost.toString()
    }

    companion object {
        class State(val pos : Vector3D, val target : Vector3D, val board : Map<Vector3D,Int>, val last3Directions : List<Vector3D>) : AStar.Vertex<CostVector>{
            override fun heuristic(): Long {
                return (target-pos).manhattan()
            }

            override fun isGoal(): Boolean {
                return pos == target
            }

            override fun applyEdge(edge: CostVector): AStar.Vertex<CostVector> {
                return State(pos + edge.direction, target,board,(last3Directions + edge.direction).takeLast(3))
            }

            override fun getEdges(): List<CostVector> {

                val uniqueLast = last3Directions.toSet()

                return Vector3D.Directions2d
                    .filter { board.containsKey(pos + it) }
                    .filter { -it != last3Directions.lastOrNull() }
                    .filter {  uniqueLast.size != 1 || it != uniqueLast.first() }
                    .map { CostVector(it, board.getValue(pos + it).toLong()) }
            }

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as State

                if (pos != other.pos) return false
                if (last3Directions != other.last3Directions) return false

                return true
            }

            override fun hashCode(): Int {
                var result = pos.hashCode()
                result = 31 * result + last3Directions.hashCode()
                return result
            }

        }
        class CostVector(val direction : Vector3D, override val cost: Long) : AStar.Edge
    }
}