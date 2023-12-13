import structure.LinesPuzzle
import utils.Grid
import kotlin.math.min

class Day13Part2 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        var listPuzzleLines = mutableListOf<String>()
        val puzzles = mutableListOf<Puzzle>()
        for (l in lines){
            if (l.isBlank()){
                puzzles.add(Puzzle(Grid.fromLines(listPuzzleLines)))
                listPuzzleLines = mutableListOf<String>()
            }
            else{
                listPuzzleLines.add(l)
            }
        }

        puzzles.add(Puzzle(Grid.fromLines(listPuzzleLines)))

        val mirrors = puzzles.map { it.checkMirror() }
        return mirrors.sum().toString()

    }

    companion object {
        data class Puzzle(val grid : Grid<Char>){

            fun checkMirror() : Int{
                val lineMirror = checkMirrorDim(grid.lineData)
                val columnMirror = checkMirrorDim(grid.columData)
                check((lineMirror == null) != (columnMirror == null)) {
                    "two reflections"
                }
                return (lineMirror?: 0) *100 + (columnMirror?: 0)
            }

            fun checkMirrorDim(lines : List<List<Char>>):Int?{
                var previousLine :List<Char>? = null
                for(i in lines.indices){
                    val diff = diff(lines[i], previousLine)
                    if(diff <= 1){
                        var match = diff
                        for (j in 1 until min(i,lines.size-i)){
                            val diff1 = diff(lines[i - j - 1], lines[i + j])
                            match += diff1
                            if(match > 1){
                                break
                            }
                        }
                        if (match == 1){
                            return i
                        }
                    }
                    previousLine = lines[i]
                }
                return null
            }
            fun diff(a : List<Char>?, b : List<Char>?): Int{
                if (a == null || b == null){
                    return Int.MAX_VALUE
                }
                return a.zip(b).filter { it.first != it.second }.size
            }
        }

    }
}