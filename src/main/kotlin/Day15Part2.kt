import structure.LinesPuzzle
import java.util.*

class Day15Part2 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {

        val hm = mutableMapOf<Long, MutableList<String>>()

         lines.first().split(',')
            .forEach() { instruction ->
                val label = instruction.split("=")[0].substringBefore('-')
                val remove = instruction.endsWith("-")
                val h = hash(label)
                if (remove) {
                    hm[h]?.removeIf { it.startsWith("$label=") }
                } else {
                    val l = hm.computeIfAbsent(h, { mutableListOf() })
                    var found = false
                    l.replaceAll {
                        if (it.startsWith("$label=")) {
                            found = true
                            instruction
                        } else {
                            it
                        }
                    }
                    if (!found){
                        l.add(instruction)
                    }
                }

            }

        val hashes = hm.map { (hash, lenses) ->
            (hash + 1) * lenses.mapIndexed { index, lens ->
                val focalLength = lens.split("=")[1].toLong()
                (index+1) * focalLength
            }.sum()
        }
        return hashes.sum().toString()
    }

    fun hash(str: String): Long {
        var result = 0L
        str.chars().forEach {
            result += it
            result *= 17
            result %= 256
        }
        return result
    }

    companion object {
        data class lens(val label: String, val focalLength: Long) {
        }
    }
}