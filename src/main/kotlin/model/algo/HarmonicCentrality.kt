package model.algo

import graph.Graph
import graph.Vertex

fun getHarmonicCentrality(
    graph: Graph,
    vertex: Vertex,
): Double? {
    var centrality = 0.0
    graph.vertices.forEach {
        if (it != vertex) {
            val dist = dijkstra(graph, vertex, it)?.first
            if (dist == null) return null
            if (dist != Double.MAX_VALUE) {
                centrality += 1 / dist
            }
        }
    }
    return centrality
}
