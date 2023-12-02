import structure.LinesPuzzle

class Day02Part1 : LinesPuzzle() {


    override fun solve(lines: List<String>): String {
        val testSet = CubeSet(12, 13, 14)
        val allGames = lines.map { Game.fromString(it) }
        val possibleGames = allGames.filter { it.possibleWithSet(testSet) }
        return possibleGames.sumOf { it.id }.toString()
    }

    companion object {
        data class Game(
            val id: Long,
            val turns: List<CubeSet>
        ) {

            fun possibleWithSet(testSet: CubeSet): Boolean {
                return turns.all { it.containedIn(testSet) }
            }

            companion object {
                fun fromString(str: String): Game {
                    val gameAndTurns = str.split("Game ")[1].split(": ")
                    return Game(
                        gameAndTurns[0].toLong(),
                        gameAndTurns[1].split(";").map { CubeSet.fromString(it) }
                    )
                }
            }
        }

        data class CubeSet(
            val red: Long,
            val green: Long,
            val blue: Long
        ) {
            fun containedIn(other: CubeSet): Boolean {
                return this.red <= other.red && this.green <= other.green && this.blue <= other.blue
            }

            companion object {
                fun fromString(str: String): CubeSet {

                    val cubes = str.split(", ").map {
                        val parts =  it.trim().split(" ")
                        parts[1] to parts[0].toLong()
                    }.toMap()

                    return CubeSet(
                        cubes["red"] ?: 0L,
                        cubes["green"] ?: 0L,
                        cubes["blue"] ?: 0L,
                    )
                }
            }
        }
    }
}