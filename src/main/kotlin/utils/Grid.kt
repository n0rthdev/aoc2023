package utils

class Grid<T>(val lineData: List<List<T>>) {
    val columData = lineData.first().indices.map { i -> lineData.map { it[i] } }

    fun getRow(i: Int): List<T> {
        return lineData[i]
    }

    fun getColumn(j: Int): List<T> {
        return columData[j]
    }



    fun rowsMatch(i: Int, j: Int, match: (List<T>, List<T>) -> Boolean = { a, b -> a == b }): Boolean {
        return match(getRow(i), getRow(j))
    }

    fun columnsMatch(i: Int, j: Int, match: (List<T>, List<T>) -> Boolean = { a, b -> a == b }): Boolean {
        return match(getColumn(i), getColumn(j))
    }

    override fun toString(): String {
        return toStringLines()
    }

    fun toStringLines(): String {
        return lineData.map { it.joinToString("") }.joinToString("\n")
    }

    fun toStringColumns(): String {
        return columData.map { it.joinToString("") }.joinToString("\n")
    }

    companion object {
        fun fromLines(lines: List<String>): Grid<Char> {
            val data = lines.map { it.toCharArray().toList() }
            return Grid<Char>(data)
        }
    }

}