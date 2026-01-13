package model.algo

import graph.Edge
import graph.Graph
import graph.Vertex

fun findBridges(graph: Graph): Set<Edge> {
    var time = 0
    val enter = mutableMapOf<Vertex, Int>()
    val ret = mutableMapOf<Vertex, Int>()
    val bridges = mutableSetOf<Edge>()

    fun dfs(
        v: Vertex,
        parent: Vertex?,
    ) {
        time++
        enter[v] = time
        ret[v] = time
        val neighbours = graph.edges[v] ?: emptySet()
        for (edge in neighbours) {
            val u = edge.to
            if (u == parent) continue
            if (enter.containsKey(u)) {
                ret[v] = minOf(ret[v]!!, enter[u]!!)
            } else {
                dfs(u, v)
                ret[v] = minOf(ret[v]!!, ret[u]!!)
                if (ret[u]!! > enter[v]!!) {
                    bridges.add(edge)
                }
            }
        }
    }
    graph.vertices.forEach { vertex ->
        if (!enter.containsKey(vertex)) {
            dfs(vertex, null)
        }
    }
    return bridges.toSet()
}
