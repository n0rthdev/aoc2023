package utils

import java.util.*

object AStar {
    fun findPath(begin: Vertex): VertexWithCost {
        val priorityQueue = PriorityQueue<VertexWithCost>()

        var currentVertex = VertexWithCost.forStart(begin)
        while (priorityQueue.isNotEmpty()) {
            // Check if we have reached the finish
            if (currentVertex.isGoal) {
                // Backtrack to generate the most efficient path
                return currentVertex
            }
            currentVertex = priorityQueue.remove()

            val newVertices = currentVertex.getNextVertices()
            priorityQueue.addAll(newVertices)
        }
        throw IllegalArgumentException("No Path")
    }
    fun traversePath(goal: VertexWithCost) :AStarResul{
        var current :VertexWithCost? = goal
        val edgeList = mutableListOf<Edge>()
        while(current?.previous != null){
            val edge = current.edge
            if(edge != null){
                edgeList.add(edge)
                current = current.previous
            }
        }
        return AStarResul(goal, edgeList.reversed())
    }
}

class VertexWithCost private constructor(val vertex: Vertex, val cost: Long, val previous: VertexWithCost?, val edge: Edge?) :
    Comparable<VertexWithCost> {
    val estimatedTotalCost = cost + vertex.heuristic()
    val isGoal get() = vertex.isGoal()

    override fun compareTo(other: VertexWithCost): Int {
        return estimatedTotalCost.compareTo(other.estimatedTotalCost)
    }

    fun getNextVertices(): List<VertexWithCost> {
        return vertex.getEdges().map {
            traverse(it)
        }
    }

    fun traverse(edge: Edge): VertexWithCost {
        return forEdge(this.vertex.applyEdge(edge), cost + edge.cost, this, edge)
    }

    companion object {
        fun forStart(vertex: Vertex) = VertexWithCost(vertex, 0L, null, null)
        fun forEdge(vertex: Vertex, cost: Long, previous: VertexWithCost, edge: Edge) = VertexWithCost(vertex, cost, previous, edge)
    }
}

data class AStarResul(val goal: VertexWithCost, val path: List<Edge>)

interface Vertex {
    fun heuristic(): Long
    fun isGoal(): Boolean
    fun applyEdge(edge: Edge): Vertex
    fun getEdges(): List<Edge>
}

interface Edge {
    val cost: Long
}