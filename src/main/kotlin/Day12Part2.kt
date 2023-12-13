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
           println(it.line +" " + arrangements)
            arrangements
        }
            .sum().toString()
    }

    companion object {
        data class Springs(val line: String, val groups: List<Int>) {
//            fun getArrangements3(): Long {
//
//                var i = 0
//                var groupStart = -1
//                var groupMinIdx = 0
//
//                while (true) {
//                    val charat = line[i]
//
//                    if (charat == '?' || charat == '#') {
//                        if (groupStart == -1) {
//                            groupStart = i
//                        } else {
//
//                        }
//                    } else {
//                        if (groupStart != -1) {
//                            val mygroup = line.substring(groupStart, i)
//                        }
//                        groupStart = -1
//                    }
//                    i++
//                }
//                return 0L
//            }

            fun unfold() : Springs{
                return Springs(
                    line + line + line + line + line,
                    groups + groups + groups+ groups + groups
                )
            }

            fun getArrangements(): Long {
                return getArrangementsRec(line, 0, 0)
            }
//            fun getArrangementsRec(str: String, i: Int, gi: Int): Long {
//
//                var j = i
//
////                while (str[++j] == '.'){}
//
//                val nextGroup = groups[gi]
//
//                var groupStart = -1
//                var latestStart = -1
//                var groupLength = 0
////                var groupMinIdx = 0
//
//                while (j < str.length && (latestStart == -1 || j < latestStart + nextGroup)){
//                    val charat = str[j]
//                    if (charat == '?' || charat == '#') {
//                        if (groupStart == -1) {
//                            groupStart = j
//                        }
//                        if(charat == '#' && latestStart == -1) {
//                            latestStart = j
//                        }
//                        groupLength++
//                    } else {
//                        if (groupStart != -1) {
//                            break
//                        }
//                    }
//                    j++
//                }
//
//            }

            fun getArrangementsRec(str: String, i: Int, gi: Int): Long {

                var j = i

//                while (str[++j] == '.'){}

                if (groups.size == gi){
                    if(j<str.length && str.substring(j).contains('#')){
                        return 0
                    }
                    else{
                        return 1
                    }
                }

                val nextGroup = groups[gi]


                while (j < str.length) {
                    val charat = str[j]
                    if (charat == '?' && j+nextGroup <= str.length && !str.substring(j,j+nextGroup).contains('.') && (j+nextGroup == str.length || str[j+nextGroup] != '#')){
                        return getArrangementsRec(str,j + nextGroup+1,gi +1) + getArrangementsRec(str,j + 1,gi)
                    }
                    if (charat == '#') {
                        if(j+nextGroup > str.length || str.substring(j,j+nextGroup).contains('.') || (j+nextGroup != str.length && str[j+nextGroup] == '#')){
                            return 0
                        }
                        return getArrangementsRec(str,j + nextGroup+1,gi +1)
                    }
                    j++
                }
                return 0
            }



            fun getArrangementsRec1(str: String, i: Int, gi: Int): Long {
                if (i == str.length) {
                    if (getGroups(str) == groups) {
                        return 1
                    } else {
                        return 0
                    }
                } else if (str[i] == '?') {
                    val a = str.substring(0, i) + "#" + str.substring(i + 1)
                    val b = str.substring(0, i) + "." + str.substring(i + 1)
                    val ar = getArrangementsRec1(a, i + 1, 0)
                    val br = getArrangementsRec1(b, i + 1, 0)
                    return ar + br
                } else {
                    return getArrangementsRec1(str, i + 1, 0)
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