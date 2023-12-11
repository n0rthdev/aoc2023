import structure.LinesPuzzle
import utils.Vector3D

class Day10Part1 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val grid = mutableMapOf<Vector3D,Pipe>()
        var starts :Vector3D? = null

        lines.forEachIndexed { row, s ->
            s.forEachIndexed { col, c ->
                val pos = Vector3D(col,lines.size-1-row,0)
                if(c == 'S'){
                    check(starts==null)
                    starts = pos
                }
                else {
                    val pipe = when (c) {
                        '|' -> Pipe(Vector3D.NORTH, Vector3D.SOUTH)
                        '-' -> Pipe(Vector3D.EAST, Vector3D.WEST)
                        'L' -> Pipe(Vector3D.NORTH, Vector3D.EAST)
                        'J' -> Pipe(Vector3D.NORTH, Vector3D.WEST)
                        '7' -> Pipe(Vector3D.SOUTH, Vector3D.WEST)
                        'F' -> Pipe(Vector3D.SOUTH, Vector3D.EAST)
                        '.' -> null
                        else -> throw IllegalArgumentException()
                    }
                    if (pipe != null) {
                        grid[pos] = pipe
                    }
                }
            }
        }

        val start = checkNotNull(starts)

        val startDirections = Vector3D.Directions2d.mapNotNull { dir -> grid[start+dir]?.connections?.firstOrNull() { -it == dir } }.map { -it }

        check(startDirections.size == 2)

        var counter =1
        var dir = startDirections.first()
        var pos = start + dir

        while (pos!=start){
            dir = grid.getValue(pos).getOtherDirection(dir)
            pos = pos + dir
            counter++
        }
//
//        val startIdx =lines.flatMap { it.toCharArray().toList()}.indexOf('S')
//        val startX =startIdx% lines.first().length
//        val startY =startIdx/ lines.first().length
//
//        val start = Vector3D(startX.toLong(),startY.toLong(),0)
//
//        val trip = mutableMapOf<Vector3D,Long>()

        return (counter/2).toString()
    }

    data class Pipe(val a :Vector3D, val b :Vector3D){
        val connections get() = setOf(a,b)

        fun getOtherDirection(dir :Vector3D) :Vector3D{
            val others = connections.filterNot { it == -dir }
            check(others.size==1)
            return others.first()
        }
    }

    companion object {

    }
}