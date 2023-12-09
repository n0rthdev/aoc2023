import structure.LinesPuzzle

class Day09Part2 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val sequenceList = lines.map { Sequence.fromString(it) }
        val estimates = sequenceList.map { getValue(it) }
        return estimates.sum().toString()
    }

    fun getValue(sequence: Sequence):Long{
        if(sequence.isZeros()){
            return 0
        }
        else{
            val diff = getValue(sequence.diffs())
            return sequence.getFirst() - diff
        }
    }

    companion object {
        data class Sequence(val numbers :List<Long>){

            fun diffs() = Sequence(numbers.windowed(2,1).map { it.last() - it.first()})

            fun isZeros() = numbers.all { it == 0L }

            fun getFirst() = numbers.first()

            companion object{
                fun fromString(str:String):Sequence{
                    return Sequence(str.split(" ").map { it.toLong() })
                }
            }
        }
    }
}