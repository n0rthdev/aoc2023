import structure.LinesPuzzle
import utils.Grid
import kotlin.math.min

class Day13Part1 : LinesPuzzle() {
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
                var lastLine :List<Char>? = null
                for(i in lines.indices){
                    if(lines[i] == lastLine){
                        var match = true
                        for (j in 1 until min(i,lines.size-i)){
                            if(lines[i-j-1] != lines[i+j]){
                                match = false
                                break
                            }
                        }
                        if (match){
                            return i
                        }
                    }
                    lastLine = lines[i]
                }
                return null
            }
        }

    }
}