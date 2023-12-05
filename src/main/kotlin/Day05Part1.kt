import structure.LinesPuzzle

class Day05Part1 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val seeds = lines[0].substringAfter("seeds: ").split(" ").map { it.toLong() }.toList().sorted()

        var lineIdx = 2
        val game = mutableListOf<MutableList<Myrange>>()

        while(lineIdx < lines.size) {
            if(lines[lineIdx].isEmpty()){
                //next map
            }
            else if(lines[lineIdx].contains("-to-")) {
                game.add(mutableListOf<Myrange>())
            }
            else {
                val map = lines[lineIdx].split(" ").map { it.toLong() }
                game.last().add(
                    Myrange(map[1], map[0], map[2])
                )
            }
            lineIdx++
        }

        game.forEach {
            it.sortBy { it.src }
        }

        var pos = 0

        var stageSeeds = seeds

        game.forEach {stage ->
                val newStageSeeds = mutableListOf<Long>()
                var translationIdx = 0
                var i = 0
                while(i < stageSeeds.size) {
                    val currentSeed = stageSeeds[i]
                    if(translationIdx >= stage.size) {
                        newStageSeeds.add(currentSeed)
                        i++
                    }
                    else {
                        val currentStage = stage[translationIdx]
                        if(currentSeed < currentStage.src) {
                            newStageSeeds.add(currentSeed)
                            i++
                        }
                        else if(currentSeed >= currentStage.src && currentSeed <= currentStage.srcEnd) {
                            newStageSeeds.add(currentSeed + currentStage.translate)
                            i++
                        }
                        else if(currentSeed > currentStage.srcEnd) {
                            translationIdx++
                        }
                    }
                }
            stageSeeds = newStageSeeds.sorted()
        }


        return stageSeeds.min().toString();
    }

    companion object {
        data class Myrange(val src : Long, val dest : Long, val spread : Long)
        {
            val srcEnd get() = src + spread - 1
            val destEnd get() = dest + spread - 1
            val translate get() = dest - src
        }
    }
}