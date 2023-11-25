package structure

import java.io.File
import java.util.*

sealed class Puzzle {
    val day: String
    val part: String

    init {
        val match = CLASSNAME_SPLIT_REGEX.find(this.javaClass.name)!!
        val (day, part) = match.destructured
        this.day = day
        this.part = part
    }

    val dayNumber = day.toInt()
    val name get() = "Day: $day Part: $part"

    abstract fun solveFile(inputFile: File): String

    fun solveExample(): String {
        return solveFile(File("puzzles/$day/example.in"))
    }

    fun solveMy(): String {
        return solveFile(File("puzzles/$day/my.in"))
    }

    companion object {
        val CLASSNAME_SPLIT_REGEX =
            """Day(\d{2})Part(\d{1})""".toRegex()
    }
}

abstract class ScannerPuzzle : Puzzle() {

    override fun solveFile(inputFile: File): String {
        val scanner = Scanner(inputFile)
        return solve(scanner)
    }

    protected abstract fun solve(scanner: Scanner): String

}

abstract class LinesPuzzle : Puzzle() {

    override fun solveFile(inputFile: File): String {
        return solve(inputFile.readLines())
    }

    protected abstract fun solve(lines: List<String>): String

}


