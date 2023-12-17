import structure.LinesPuzzle
import utils.Vector3D

class Day16Part2 : LinesPuzzle() {
    override fun solve(lines: List<String>): String {
        val height = lines.size
        val width = lines[0].length

        val tiles = mutableMapOf<Vector3D, Tile>()

        for (li in lines.indices) {
            val y = height - li - 1
            for (x in lines[li].indices) {
                val tile = lines[li][x]
                tiles[Vector3D(x, y, 0)] = Tile(tile)
            }
        }

        val lasers = (0 until width).flatMap {
            listOf(
                Laser(Vector3D(it.toLong(), (height - 1).toLong(), 0), Vector3D.SOUTH),
                Laser(Vector3D(it.toLong(), 0, 0), Vector3D.NORTH),
            )
        } + (0 until height).flatMap {
            listOf(
                Laser(Vector3D((width - 1).toLong(), it.toLong(), 0), Vector3D.WEST),
                Laser(Vector3D(0L, it.toLong(), 0), Vector3D.EAST),
            )
        }

        val results = lasers.associateWith {
            val start = setOf(it)
            doLasers(start, width, height, tiles)
        }

        return results.values.max().toString()
    }

    private fun doLasers(
        lasers: Set<Laser>,
        width: Int,
        height: Int,
        tiles: MutableMap<Vector3D, Tile>,
    ): Int {
        val laserSet = mutableMapOf<Set<Laser>, Int>()
        var lasers1 = lasers
        val visitedTiles = mutableSetOf<Vector3D>()
        var time = 0L
        while (lasers1.isNotEmpty()) {
            lasers1 = lasers1.flatMap { laser ->
                if (laser.pos.x < 0 || laser.pos.y < 0 || laser.pos.x >= width || laser.pos.y >= height) {
                    emptyList()
                } else {
                    visitedTiles.add(laser.pos)
                    val tile = tiles.getValue(laser.pos)
                    tile.traverse(laser.direction).map { Laser(laser.pos.move(it), it) }
                }
            }.toSet()
            if (laserSet.contains(lasers1)) {
                if (visitedTiles.size > laserSet.getValue(lasers1)) {
                    laserSet[lasers1] = visitedTiles.size
                }
                break
            }
            laserSet[lasers1] = visitedTiles.size
            time++
        }
        return laserSet.values.max()
    }

    companion object {
        data class Laser(val pos: Vector3D, val direction: Vector3D)

        data class Tile(val type: Char) {
            fun traverse(inputDirection: Vector3D): List<Vector3D> {
                return when (type) {
                    '/' -> when (inputDirection) {
                        Vector3D.EAST -> listOf(Vector3D.NORTH)
                        Vector3D.NORTH -> listOf(Vector3D.EAST)
                        Vector3D.WEST -> listOf(Vector3D.SOUTH)
                        Vector3D.SOUTH -> listOf(Vector3D.WEST)
                        else -> throw IllegalArgumentException(inputDirection.toString())
                    }

                    '\\' -> when (inputDirection) {
                        Vector3D.EAST -> listOf(Vector3D.SOUTH)
                        Vector3D.NORTH -> listOf(Vector3D.WEST)
                        Vector3D.WEST -> listOf(Vector3D.NORTH)
                        Vector3D.SOUTH -> listOf(Vector3D.EAST)
                        else -> throw IllegalArgumentException(inputDirection.toString())
                    }

                    '|' -> when (inputDirection) {
                        Vector3D.EAST, Vector3D.WEST -> listOf(Vector3D.NORTH, Vector3D.SOUTH)
                        else -> listOf(inputDirection)
                    }

                    '-' -> when (inputDirection) {
                        Vector3D.NORTH, Vector3D.SOUTH -> listOf(Vector3D.EAST, Vector3D.WEST)
                        else -> listOf(inputDirection)
                    }

                    '.' -> listOf(inputDirection)
                    else -> throw IllegalArgumentException(type.toString())
                }
            }
        }
    }
}