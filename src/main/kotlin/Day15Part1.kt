import structure.LinesPuzzle

class Day15Part1 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val hashes = lines.first().split(',').map { hash(it) }
        return hashes.sum().toString()
    }

    fun hash(str : String) : Long{
        var result = 0L
        str.chars().forEach {
            result += it
            result *= 17
            result%=256
        }
        return result
    }

    companion object {

    }
}