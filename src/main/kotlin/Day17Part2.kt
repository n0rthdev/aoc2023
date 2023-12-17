import structure.LinesPuzzle
import utils.AStar
import utils.Vector3D

class Day17Part2 : LinesPuzzle() {
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

        val state = State(start,target, tiles,null)

        return AStar<CostVector>().findPath(state).cost.toString()
    }

    companion object {
        class State(val pos : Vector3D, val target : Vector3D, val board : Map<Vector3D,Int>, val lastDirection : Vector3D?) : AStar.Vertex<CostVector>{
            override fun heuristic(): Long {
                return (target-pos).manhattan()
            }

            override fun isGoal(): Boolean {
                return pos == target
            }

            override fun applyEdge(edge: CostVector): AStar.Vertex<CostVector> {
                return State(pos + edge.direction * edge.multiple, target,board,edge.direction)
            }

            override fun getEdges(): List<CostVector> {

                return Vector3D.Directions2d
                    .filter { lastDirection == null || (it != -lastDirection && it != lastDirection )}
                    .flatMap { dir ->
                        var costs = 0L
                        val list = mutableListOf<CostVector>()
                        for (i in 1L..10L){
                            val cpos = pos + (dir * i)
                            if (!board.containsKey(cpos)){
                                break
                            }
                            costs += board.getValue(cpos)
                            if(i >= 4){
                                list.add(CostVector(dir,i,costs))
                            }
                        }
                        list
                    }
            }

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as State

                if (pos != other.pos) return false
                if (lastDirection != other.lastDirection) return false

                return true
            }

            override fun hashCode(): Int {
                var result = pos.hashCode()
                result = 31 * result + lastDirection.hashCode()
                return result
            }


        }
        class CostVector(val direction : Vector3D, val multiple : Long, override val cost: Long) : AStar.Edge
    }
}