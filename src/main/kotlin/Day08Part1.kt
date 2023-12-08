import structure.LinesPuzzle
import java.util.stream.IntStream.range

class Day08Part1 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {

        val directions = lines[0].toCharArray()
        val mymap = mutableMapOf<String,Node>()
        for (i in range(2, lines.size)) {
            val node = Node.from(lines[i])
            mymap[node.name] = node
        }

        val cnt = shortestPath(mymap.getValue("AAA"), mymap.getValue("ZZZ"), directions, mymap)
        return cnt.toString()
    }

    private fun shortestPath(
        start: Node,
        target: Node,
        directions: CharArray,
        mymap: MutableMap<String, Node>
    ): Long {
        var current = start
        var cnt = 0L

        while (current != target) {
            for (d in directions) {
                val next = when (d) {
                    'L' -> current.left
                    'R' -> current.right
                    else -> throw RuntimeException("invalid direction")
                }
                current = mymap.getValue(next)
                cnt++
                if (current == target) {
                    break
                }
            }
        }
        return cnt
    }

    companion object {
        data class Node(val name: String, val left: String, val right: String){

            companion object{
                val SPLIT_REGEX =
                    """(\w{3}) = \((\w{3}), (\w{3})\)""".toRegex()
                fun from(str:String): Node {
                    val matchResult = SPLIT_REGEX.find(str)
                    val (a,b,c) = matchResult!!.destructured
                    return Node(a,b,c)
                }
            }
        }
    }
}