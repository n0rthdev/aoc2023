import structure.LinesPuzzle
import utils.Vector3D

class Day10Part2 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val grid = mutableMapOf<Vector3D,Pipe>()
        val tiles = mutableSetOf<Vector3D>()
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
                        '.' -> {
                            tiles.add(pos)
                            null
                        }
                        else -> throw IllegalArgumentException("invalid char $c")
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

        grid[start] = Pipe(startDirections.first(),startDirections.last())

        var counter =0
        var dir = -startDirections.first()
        var pos = start

        val mainPipe = mutableMapOf<Vector3D,Vector3D>()

        do{
            dir = grid.getValue(pos).getOtherDirection(dir)
            pos += dir
            counter++
            mainPipe.put(pos,dir)
        }while (pos!=start)

        var countInner =0

        for(i in lines.indices){
            var pipeing = false
            var enterY = 0L
            var inLoop = false
            for (j in lines.first().indices) {
                val cur = Vector3D(j,lines.size-1-i,0)
                 if (mainPipe.contains(cur)){
                    val pipe = grid.getValue(cur)
                    val thisy = pipe.connections.sumOf { it.y }
                    if (pipe == Pipe(Vector3D.NORTH, Vector3D.SOUTH)){
                        inLoop = !inLoop
                    }
                    else if (!pipeing){
                        enterY = thisy
                        pipeing = true
                    }
                    else {
                        if(thisy == -enterY) {
                            inLoop = !inLoop
                        }
                        if (thisy != 0L){
                            pipeing = false
                            enterY = 0L
                        }
                    }
                }
                else if(inLoop){
                    countInner++
                }
                else {
                    check(!pipeing)
                }
            }
        }

        return (countInner).toString()
    }

    data class Pipe(val connections : Set<Vector3D>){
        constructor(vararg vs: Vector3D) : this(vs.toSet())
        fun getOtherDirection(dir :Vector3D) :Vector3D{
            val others = connections.filterNot { it == -dir }
            check(others.size==1)
            return others.first()
        }
    }

    companion object {

    }
}