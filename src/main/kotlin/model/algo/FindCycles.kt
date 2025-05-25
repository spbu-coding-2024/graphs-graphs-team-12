package algo

import graph.Graph
import graph.Vertex

fun findCycles(
    graph: Graph,
    start: Vertex,
): Set<List<Vertex>> {
    val cycles = mutableSetOf<List<Vertex>>()
    val path = mutableListOf<Vertex>()

    fun dfs(current: Vertex) {
        path.add(current)

        graph.edges[current]?.forEach({
            val prev = if (path.size >= 2) path[path.size - 2] else null

            if (it.to != prev || graph.directed) {
                if (it.to == start) {
                    cycles.add(path.toList())
                } else {
                    dfs(it.to)
                }
            }
        })

        path.removeLast()
    }

    dfs(start)
    return cycles
}
