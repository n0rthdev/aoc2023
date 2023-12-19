package utils

import kotlin.math.roundToLong

data class Vector3D(val x: Long, val y: Long, val z: Long) {

    constructor(x: Int, y: Int, z: Int) : this(x.toLong(), y.toLong(), z.toLong())

    operator fun unaryMinus(): Vector3D {
        return Vector3D(-x, -y, -z)
    }

    operator fun unaryPlus(): Vector3D {
        return this
    }

    operator fun plus(other: Vector3D): Vector3D {
        return Vector3D(x + other.x, y + other.y, z + other.z)
    }

    operator fun minus(other: Vector3D): Vector3D {
        return Vector3D(x - other.x, y - other.y, z - other.z)
    }

    operator fun times(times: Long): Vector3D {
        return Vector3D(x * times, y * times, z * times)
    }

    operator fun times(times: Int): Vector3D {
        return times(times.toLong())
    }

    operator fun div(div: Long): Vector3D {
        return Vector3D(x / div, y / div, z / div)
    }

    fun move(dir: Vector3D, count: Long = 1L) = this + (dir * count)

    fun right(count: Long = 1L) = move(RIGHT, count)
    fun left(count: Long = 1L) = move(LEFT, count)
    fun forward(count: Long = 1L) = move(FORWARD, count)
    fun backward(count: Long = 1L) = move(BACKWARD, count)
    fun up(count: Long = 1L) = move(UP, count)
    fun down(count: Long = 1L) = move(DOWN, count)

    fun euclidean(): Double {
        return Math.sqrt(Math.pow(x.toDouble(), 2.0) + Math.pow(y.toDouble(), 2.0) + Math.pow(z.toDouble(), 2.0))
    }

    fun euclideanLong(): Long {
        return euclidean().roundToLong()
    }

    fun manhattan(): Long {
        return abs().sum()
    }

    fun gridDistance(): Long {
        return abs().max()
    }

    fun inCube(corner1: Vector3D, corner2: Vector3D): Boolean {
        return xInRangeIncluding(corner1.x, corner2.x) &&
                yInRangeIncluding(corner1.y, corner2.y) &&
                zInRangeIncluding(corner1.z, corner2.z)
    }

    fun xInRangeIncluding(x1: Long, x2: Long): Boolean {
        val min = Math.min(x1, x2)
        val max = Math.max(x1, x2)

        return min <= x && x <= max
    }

    fun yInRangeIncluding(y1: Long, y2: Long): Boolean {
        val min = Math.min(y1, y2)
        val max = Math.max(y1, y2)

        return min <= y && y <= max
    }

    fun zInRangeIncluding(z1: Long, z2: Long): Boolean {
        val min = Math.min(z1, z2)
        val max = Math.max(z1, z2)

        return min <= z && z <= max
    }

    fun abs() = Vector3D(Math.abs(x), Math.abs(y), Math.abs(z))

    fun sum() = x + y + z

    fun min() = Math.min(Math.min(x, y), z)

    fun max() = Math.max(Math.max(x, y), z)

    companion object {
        val ZERO = Vector3D(0, 0, 0)
        val X_ONE = Vector3D(1, 0, 0)
        val Y_ONE = Vector3D(0, 1, 0)
        val Z_ONE = Vector3D(0, 0, 1)


        val RIGHT = X_ONE
        val LEFT = -X_ONE
        val FORWARD = Y_ONE
        val BACKWARD = -Y_ONE

        val NORTH = FORWARD
        val SOUTH = BACKWARD
        val EAST = RIGHT
        val WEST = LEFT

        val UP = Z_ONE
        val DOWN = -Z_ONE

        val DirectionsX = listOf(RIGHT, LEFT)
        val DirectionsY = listOf(FORWARD, BACKWARD)
        val DirectionsZ = listOf(UP, DOWN)

        val Directions1d = DirectionsX
        val Directions2d = DirectionsX + DirectionsY
        val Directions3d = DirectionsX + DirectionsY + DirectionsZ

        val Directions2dWithDiagonals = Directions2d + DirectionsX.flatMap { x -> DirectionsY.map { y -> x + y } }
        val Directions3dWithDiagonals =
            Directions3d + DirectionsX.flatMap { x -> DirectionsY.flatMap { y -> DirectionsZ.map { z -> x + y + z } } }


        fun fromString(str: String, separator: String = ",", dim: Int = 3): Vector3D {
            require(dim in 1..3) { "dim must be between 0 and 3" }
            try {
                val parts = str.split(separator).map { it.trim().toLong() }
                check(parts.size == dim)
                return Vector3D(parts.getOrNull(0) ?: 0, parts.getOrNull(1) ?: 0, parts.getOrNull(2) ?: 0)
            } catch (ex: Exception) {
                throw IllegalArgumentException("Could not parse $str with $separator to Vector3D($dim)", ex)
            }

        }

        class DirectionParser(
            val right: String,
            val left: String,
            val forward: String,
            val backward: String,
            val up: String,
            val down: String
        ) {
            fun parse(ch: Char): Vector3D {
                return parse(ch.toString())
            }

            fun parse(str: String): Vector3D {
                return when (str) {
                    right -> RIGHT
                    left -> LEFT
                    forward -> FORWARD
                    backward -> BACKWARD
                    up -> UP
                    down -> DOWN
                    else -> throw IllegalArgumentException("String $str is not parsable")
                }
            }

            companion object {
                fun fromChars(chars: String = "RLFBUD"): DirectionParser {
                    val strings = chars.map { it.toString() }
                    return DirectionParser(
                        strings[0],
                        strings[1],
                        strings[2],
                        strings[3],
                        strings[4],
                        strings[5]
                    )
                }
            }
        }
    }
}