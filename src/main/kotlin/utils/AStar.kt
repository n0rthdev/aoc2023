package utils

import java.util.*

class AStar<E: AStar.Edge> {
    fun findPath(begin: Vertex<E>): VertexWithCost<E> {
        val priorityQueue = PriorityQueue<VertexWithCost<E>>()

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
    fun traversePath(goal: VertexWithCost<E>) :AStarResul<E>{
        var current :VertexWithCost<E>? = goal
        val edgeList = mutableListOf<E>()
        while(current?.previous != null){
            val edge = current.edge
            if(edge != null){
                edgeList.add(edge)
                current = current.previous
            }
        }
        return AStarResul(goal, edgeList.reversed())
    }

    class VertexWithCost<E: Edge> private constructor(val vertex: Vertex<E>, val cost: Long, val previous: VertexWithCost<E>?, val edge: E?) :
        Comparable<VertexWithCost<E>> {
        val estimatedTotalCost = cost + vertex.heuristic()
        val isGoal get() = vertex.isGoal()

        override fun compareTo(other: VertexWithCost<E>): Int {
            return estimatedTotalCost.compareTo(other.estimatedTotalCost)
        }

        fun getNextVertices(): List<VertexWithCost<E>> {
            return vertex.getEdges().map {
                traverse(it)
            }
        }

        fun traverse(edge: E): VertexWithCost<E> {
            return forEdge(this.vertex.applyEdge(edge), cost + edge.cost, this, edge)
        }

        companion object {
            fun <E: Edge>forStart(vertex: Vertex<E>) = VertexWithCost<E>(vertex, 0L, null, null)
            fun <E: Edge>forEdge(vertex: Vertex<E>, cost: Long, previous: VertexWithCost<E>, edge: E) = VertexWithCost(vertex, cost, previous, edge)
        }
    }

    data class AStarResul<E: Edge>(val goal: VertexWithCost<E>, val path: List<E>)

    interface Vertex<E: Edge> {
        fun heuristic(): Long
        fun isGoal(): Boolean
        fun applyEdge(edge: E): Vertex<E>
        fun getEdges(): List<E>
    }

    interface Edge {
        val cost: Long
    }
}

