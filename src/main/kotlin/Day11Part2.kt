import structure.LinesPuzzle
import utils.Vector3D

class Day11Part2 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val galaxies = mutableSetOf<Galaxy>()
        val linesExpand = lines.indices.toMutableSet()
        val columnsExpand = lines.first().indices.toMutableSet()
        lines.forEachIndexed { i, line ->
            line.toCharArray().forEachIndexed { j, c ->
                if (c == '#') {
                    linesExpand.remove(i)
                    columnsExpand.remove(j)
                }
            }
        }

        var galaxy = 1L
        var y = 0L
        val emptyExpansion = 1000000L

        lines.forEachIndexed { i, line ->
            var x = 0L
            line.toCharArray().forEachIndexed { j, c ->
                if (c == '#') {
                    galaxies.add(Galaxy(galaxy++, Vector3D(x, y, 0)))
                }
                if (columnsExpand.contains(j)) {
                    x += emptyExpansion
                } else {
                    x++
                }
            }
            if (linesExpand.contains(i)) {
                y += emptyExpansion
            } else {
                y++
            }
        }
        val galaxyList = galaxies.toList()

        val distances = mutableListOf<Long>()

        for (i in galaxyList.indices) {
            for (j in i + 1 until galaxyList.size) {
                val dist = (galaxyList[i].pos - galaxyList[j].pos).manhattan()
//                println("Between galaxy ${galaxyList[i].number} and galaxy ${galaxyList[j].number}: $dist")
                distances.add(dist)
            }
        }

        return distances.sum().toString()
    }

    companion object {
        data class Galaxy(val number: Long, val pos: Vector3D)
    }
}