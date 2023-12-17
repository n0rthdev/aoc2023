package utils

import java.util.*

class AStar<E: AStar.Edge> {
    fun findPath(begin: Vertex<E>): VertexWithCost {
        val priorityQueue = PriorityQueue<VertexWithCost>()
        val vistied = mutableSetOf<Vertex<E>>()
        priorityQueue.add(forStart(begin))

        while (priorityQueue.isNotEmpty()) {
            val currentVertex = priorityQueue.remove()
            if(vistied.contains(currentVertex.vertex)){
                continue
            }
            vistied.add(currentVertex.vertex)
            // Check if we have reached the finish
            if (currentVertex.isGoal) {
                // Backtrack to generate the most efficient path
                return currentVertex
            }

            val newVertices = currentVertex.getNextVertices()
            priorityQueue.addAll(newVertices)
        }
        throw IllegalArgumentException("No Path")
    }
    fun traversePath(goal: VertexWithCost) :AStarResul{
        var current :VertexWithCost? = goal
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

    inner class VertexWithCost constructor(val vertex: Vertex<E>, val cost: Long, val previous: VertexWithCost?, val edge: E?) :
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

        fun traverse(edge: E): VertexWithCost {
            return forEdge(this.vertex.applyEdge(edge), cost + edge.cost, this, edge)
        }

    }
    fun forStart(vertex: Vertex<E>) = VertexWithCost(vertex, 0L, null, null)
    fun forEdge(vertex: Vertex<E>, cost: Long, previous: VertexWithCost, edge: E) = VertexWithCost(vertex, cost, previous, edge)

    inner class AStarResul(val goal: VertexWithCost, val path: List<E>)

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

