import structure.LinesPuzzle
import java.util.stream.IntStream.range

class Day08Part2 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {

        val directions = lines[0].toCharArray()
        val mymap = mutableMapOf<String,Node>()
        for (i in range(2, lines.size)) {
            val node = Node.from(lines[i])
            mymap[node.name] = node
        }
        val starts = mymap.values.filter { it.isStart }.toSet()
        val targets = mymap.values.filter { it.isTarget }.toSet()

        val distances = mutableMapOf<Node,MutableMap<Node,Long>>()

        for (start in starts){
            for(target in targets){
                val shortestPath = shortestPath(start, target, directions, mymap)
                if(shortestPath != null){
                    distances.computeIfAbsent(start) {
                        mutableMapOf<Node,Long>()}[target] = shortestPath
                }
            }
        }

        val cnt = findLCMOfListOfNumbers(distances.flatMap { it.value.values })
        return cnt.toString()
    }

    fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
        var result = numbers[0]
        for (i in 1 until numbers.size) {
            result = findLCM(result, numbers[i])
        }
        return result
    }

    private fun shortestPath(
        start: Node,
        target: Node,
        directions: CharArray,
        myMap: Map<String, Node>
    ): Long? {
        var current = start
        var cnt = 0L
        val visited = mutableSetOf<Pair<Node,Int>>()

        while (true) {
            for ((i,d) in directions.withIndex()) {
                if(visited.contains((Pair(current,i)))){
                    return null
                }
                val next = when (d) {
                    'L' -> current.left
                    'R' -> current.right
                    else -> throw RuntimeException("invalid direction")
                }
                visited.add(Pair(current,i))
                current = myMap.getValue(next)
                cnt++
                if (current == target) {
                    return cnt
                }
            }
        }
    }

    companion object {
        data class Node(val name: String, val left: String, val right: String){

            val isStart get() = name.endsWith('A')
            val isTarget get() = name.endsWith('Z')

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