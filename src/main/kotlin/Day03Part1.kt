import structure.LinesPuzzle

class Day03Part1 : LinesPuzzle() {

    companion object {
        data class Vector2D(val x: Long, val y: Long) {
            fun adjecentTo(other: Vector2D): Boolean {
                return Math.abs(other.x - this.x) <= 1 && Math.abs(other.y - this.y) <= 1
            }

            fun plus(other: Vector2D): Vector2D {
                return Vector2D(x + other.x, y + other.y)
            }
        }

        data class Symbol(val pos: Vector2D, val symbol: Char)
        data class MyNumber(val pos: Vector2D, val digits: String) {
            val value get() = digits.toLong()
            val allPos
                get() = digits.indices.map { pos.plus(Vector2D(it.toLong(), 0L)) }

            fun addDigitToEnd(digit: Char): MyNumber {
                return MyNumber(this.pos, this.digits + digit)
            }
        }
    }

    override fun solve(lines: List<String>): String {

        val numbers = mutableMapOf<Vector2D, MyNumber>()
        val symbols = mutableMapOf<Vector2D, Symbol>()
        lines.forEachIndexed { row, line ->
            var currentNumber: MyNumber? = null
            line.forEachIndexed { col, cc ->
                if (cc.isDigit()) {
                    if (currentNumber == null) {
                        currentNumber = MyNumber(Vector2D(col.toLong(), row.toLong()), "" + cc)
                    } else {
                        currentNumber = currentNumber!!.addDigitToEnd(cc)
                    }
                } else {
                    if (currentNumber != null) {
                        numbers.put(currentNumber!!.pos, currentNumber!!)
                    }
                    if (cc != '.') {
                        val sym = Symbol(Vector2D(col.toLong(), row.toLong()), cc)
                        symbols.put(sym.pos, sym)
                    }
                    currentNumber = null
                }
            }
            if (currentNumber != null) {
                numbers.put(currentNumber!!.pos, currentNumber!!)
            }
        }

        val result = numbers.filter { it.value.allPos.any { numberPos -> symbols.keys.any { it.adjecentTo(numberPos) } } }.values.sumOf { it.value }
        return result.toString()
    }
}