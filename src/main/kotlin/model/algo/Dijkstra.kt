package model.algo

import graph.Graph
import graph.Vertex
import java.util.PriorityQueue

fun dijkstra(
    graph: Graph,
    start: Vertex,
    end: Vertex,
): Pair<Double, List<Vertex>>? {
    for (edges in graph.edges.values) {
        for (edge in edges) {
            if (edge.weight < 0) return null
        }
    }

    val dist = mutableMapOf<Vertex, Double>()
    val parent = mutableMapOf<Vertex, Vertex?>()
    val visited = mutableSetOf<Vertex>()
    val pq = PriorityQueue<Vertex>(compareBy { dist[it] ?: Double.MAX_VALUE })

    for (vertex in graph.vertices) {
        dist[vertex] = Double.MAX_VALUE
    }
    dist[start] = 0.0
    pq.add(start)

    while (pq.isNotEmpty()) {
        val current = pq.remove()
        if (current in visited) continue
        visited.add(current)

        for (edge in graph.edges[current] ?: continue) {
            val next = edge.to
            val newDist = dist.getValue(current) + edge.weight
            if (dist.getValue(next) > newDist) {
                dist[next] = newDist
                parent[next] = current
                pq.remove(next)
                pq.add(next)
            }
        }
    }

    if (dist.getValue(end) == Double.MAX_VALUE) return Double.MAX_VALUE to emptyList()

    val path = mutableListOf<Vertex>()
    var vertex: Vertex? = end
    while (vertex != null) {
        path.add(vertex)
        vertex = parent[vertex]
    }
    path.reverse()

    return dist.getValue(end) to path
}
