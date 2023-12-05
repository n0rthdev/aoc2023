import structure.LinesPuzzle

class Day05Part2 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val seeds = lines[0].substringAfter("seeds: ").split(" ").map { it.toLong() }
            .windowed(2, 2)
            .map { MyRange(it[0], it[1]) }
//            .flatMap { range(it.first, it.first + it.second).toList() }
            .toList().sortedBy { it.start }

        var lineIdx = 2
        val game = mutableListOf<MutableList<MyTranslation>>()

        while (lineIdx < lines.size) {
            if (lines[lineIdx].isEmpty()) {
                //next map
            } else if (lines[lineIdx].contains("-to-")) {
                game.add(mutableListOf<MyTranslation>())
            } else {
                val map = lines[lineIdx].split(" ").map { it.toLong() }
                game.last().add(
                    MyTranslation(map[1], map[0], map[2])
                )
            }
            lineIdx++
        }

        game.forEach {
            it.sortBy { it.src }
        }

        var stageSeeds = seeds.toMutableList()

        game.forEach { stage ->
            val newStageSeeds = mutableListOf<MyRange>()
            var translationIdx = 0
            var i = 0
            while (i < stageSeeds.size) {
                val currentSeed = stageSeeds[i]
                if (translationIdx >= stage.size) {
                    newStageSeeds.add(currentSeed)
                    i++
                } else {
                    val currentStage = stage[translationIdx]
                    val intersect = currentSeed.splitBy(currentStage.srcRange)
                    if (intersect.size != 1) {
                        if(intersect[0].spread > 0) {
                            newStageSeeds.add(intersect[0])
                        }
                        else
                        {
                            println("asdf")
                        }
                        if(intersect[1].spread > 0) {
                            newStageSeeds.add(intersect[1].add(currentStage.translate))
                        }
                        else
                        {
                            println("asdf")
                        }
                        if(intersect[2].spread > 0) {
                            stageSeeds[i] = intersect[2]
                        }
                        else {
                            i++
                        }
                    } else if (currentSeed.start >= currentStage.srcEnd) {
                        translationIdx++
                    } else {
                        newStageSeeds.add(intersect[0])
                        i++
                    }
                }
            }
            stageSeeds = newStageSeeds.sortedBy { it.start }.toMutableList()
        }


        return stageSeeds.minOf{it.start}.toString();
    }

    companion object {

        data class MyRange(val start: Long, val spread: Long) {
            val end get() = start + spread

            fun add(add: Long) = MyRange(start + add, spread)


            fun splitBy(other: MyRange): List<MyRange> {

                val p1 = Math.max(start, other.start)
                val p2 = Math.min(end, other.end)

                val size = p2 - p1
                return if (size > 0) {
                    listOf(
                        MyRange(start, p1 - start),
                        MyRange(p1, size),
                        MyRange(p2, end - p2)
                    )
                } else {
                    listOf(this)
                }
            }

            companion object {

            }
        }

        data class MyTranslation(val src: Long, val dest: Long, val spread: Long) {
            val srcEnd get() = src + spread
            val translate get() = dest - src

            val srcRange get() = MyRange(src, spread)
            val destRange get() = MyRange(dest, spread)
        }
    }
}