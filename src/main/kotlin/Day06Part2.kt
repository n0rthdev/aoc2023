import structure.LinesPuzzle
import java.util.stream.LongStream.range

class Day06Part2 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val splitted = lines.map { it.split(":")[1].replace(" ","").split(" ").map { it.toLong() } }
        val times = splitted[0]
        val distances = splitted[1]
        var factor = 1L

        for (i in times.indices){
            val time = times[i]
            val distance    = distances[i]
// iterative solution
//            var cnt = 0L
//            for (t in range(0L,time+1)){
//                val vel = t
//                val dist = (time - t)*vel
//                if (dist > distance){
//                    cnt++
//                }
//            }
            val p1 = ((- time + Math.sqrt(1.0*time*time-4*distance))/ -2).toLong()
            val p2 = ((- time - Math.sqrt(1.0*time*time-4*distance))/ -2).toLong()
            val cnt = Math.abs(p1-p2)
            factor*=cnt
        }
        return factor.toString()
    }

    companion object {

    }
}