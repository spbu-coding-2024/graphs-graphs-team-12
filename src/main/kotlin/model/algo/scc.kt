package model.algo
import graph.*

fun Graph.find_scc() {
    if (!this.directed) {
        throw Exception("Can't perform search of strongly connected components on not directed graph")
    }

    fun dfs_with_timer(g: Map<Vertex, Set<Pair<Vertex, Double>>>, u: Vertex, timer: Int,
                       timer_storage: MutableMap<Vertex, Int>) {
        g[u]?.forEach { dfs_with_timer(g, it.first, timer + 1, timer_storage) }
        timer_storage[u] = timer
    }

    fun invert_edges(g: Graph): Graph {
        val inverted = Graph(g.directed, g.weighted, g.multigraph)
        g.vertices.forEach { vertex -> inverted.add_vertex(vertex) }
        val g_adjacency_list = g.get_adjacency_list()
        g_adjacency_list.keys.forEach { vertex ->
            g_adjacency_list[vertex]?.forEach { edge ->
                inverted.add_edge(edge.first, vertex, edge.second) } }
        return inverted
    }

}