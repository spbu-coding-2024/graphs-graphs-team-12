package model.algo

import graph.Graph
import graph.Vertex

fun bellman(
    graph: Graph,
    start: Vertex,
    end: Vertex,
): Pair<Double, List<Vertex>>? {
    val dist = mutableMapOf<Vertex, Double>()
    val parent = mutableMapOf<Vertex, Vertex>()
    graph.vertices.forEach { dist[it] = Double.POSITIVE_INFINITY }
    dist[start] = 0.0
    val edges = graph.edges.values.flatten()
    val n = graph.vertices.size
    repeat(n - 1) {
        var changed = false
        for (edge in edges) {
            val from = edge.from
            val to = edge.to
            val weight = edge.weight
            if (dist[to]!! > dist[from]!! + weight) {
                dist[to] = dist[from]!! + weight
                parent[to] = from
                changed = true
            }
        }
        if (!changed) return@repeat
    }
    for (edge in edges) {
        val u = edge.from
        val v = edge.to
        val w = edge.weight
        if (dist[u] != Double.POSITIVE_INFINITY && dist[v]!! > dist[u]!! + w) {
            return null
        }
    }
    if (dist[end] == Double.POSITIVE_INFINITY) {
        return null
    }
    val path = mutableListOf<Vertex>()
    var current: Vertex? = end
    while (current != null) {
        path.add(current)
        if (current == start) break
        current = parent[current]
    }
    if (path.last() != start) return null
    path.reverse()

    return Pair(dist[end]!!, path)
}
