import structure.LinesPuzzle

class Day12Part1 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val springs = lines.map {
            val parts = it.split(" ")
            Springs(parts[0], parts[1].split(",").map { it.toInt() })
        }

        return springs.map { it.getArrangements() }.sum().toString()
    }

    companion object {
        data class Springs(val line: String, val groups: List<Int>) {

            fun getArrangements(): Long {
                return getArrangementsRec(line, 0)
            }

            fun getArrangementsRec(str: String, i: Int): Long {
                if (i == str.length) {
                    if (getGroups(str) == groups) {
                        return 1
                    } else {
                        return 0
                    }
                } else if (str[i] == '?') {
                    val a = str.substring(0, i) + "#" + str.substring(i + 1)
                    val b = str.substring(0, i) + "." + str.substring(i + 1)
                    val ar = getArrangementsRec(a, i + 1)
                    val br = getArrangementsRec(b, i + 1)
                    return ar + br
                } else {
                    return getArrangementsRec(str, i + 1)
                }
            }
        }


        fun getGroups(string: String): List<Int> {
            var cnt = 0
            val list = mutableListOf<Int>()
            string.toCharArray().forEach {
                if (it == '#') {
                    cnt++
                } else {
                    if (cnt != 0) {
                        list.add(cnt)
                    }
                    cnt = 0
                }
            }
            if (cnt != 0) {
                list.add(cnt)
            }
            return list
        }
    }
}