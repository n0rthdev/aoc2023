import structure.LinesPuzzle

class Day12Part2 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val springs = lines.map {
            val parts = it.split(" ")
            Springs(parts[0], parts[1].split(",").map { it.toInt() })
        }

        return springs.map {
            val unfold = it.unfold()
            val arrangements = unfold.getArrangements()
            println(it.line + " " + arrangements)
            arrangements
        }
            .sum().toString()
    }

    companion object {
        data class Springs(val line: String, val groups: List<Int>) {

            val cache = mutableMapOf<Pair<Int, Int>, Long>()
            fun unfold(): Springs {
                return Springs(
                    line + "?" + line + "?" + line + "?" + line + "?" + line,
                    groups + groups + groups + groups + groups
                )
            }

            fun getArrangements(): Long {
                return getArrangementsRec(line, 0, 0)
            }


            fun getArrangementsRec(str: String, i: Int, gi: Int): Long {

                if (cache.containsKey(Pair(i, gi))) {
                    return cache.getValue(Pair(i, gi))
                }
                var j = i

                if (groups.size == gi) {
                    if (j < str.length && str.substring(j).contains('#')) {
                        return 0
                    } else {
                        return 1
                    }
                }

                val nextGroup = groups[gi]

                while (j < str.length) {
                    val charat = str[j]
                    if (charat == '?' && j + nextGroup <= str.length && !str.substring(j, j + nextGroup)
                            .contains('.') && (j + nextGroup == str.length || str[j + nextGroup] != '#')
                    ) {
                        val r = getArrangementsRec(str, j + nextGroup + 1, gi + 1) + getArrangementsRec(str, j + 1, gi)
                        cache[Pair(i, gi)] = r
                        return r
                    }
                    if (charat == '#') {
                        if (j + nextGroup > str.length || str.substring(j, j + nextGroup)
                                .contains('.') || (j + nextGroup != str.length && str[j + nextGroup] == '#')
                        ) {
                            return 0
                        }
                        val r = getArrangementsRec(str, j + nextGroup + 1, gi + 1)
                        cache[Pair(i, gi)] = r
                        return r
                    }
                    j++
                }
                val r = 0L
                cache[Pair(i, gi)] = r
                return r
            }
        }
    }
}