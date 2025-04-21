package model.algo
import graph.*
import java.util.SortedSet
import kotlin.math.max

fun Graph.find_scc(): MutableSet<MutableSet<Vertex>> {
    if (!this.directed) {
        throw Exception("Can't perform search of strongly connected components on not directed graph")
    }

    fun dfs_with_timer(g: Map<Vertex, Set<Pair<Vertex, Double>>>, u: Vertex, timer: Int,
                       timer_storage: MutableMap<Vertex, Int>): Int {
        var vertex_timer = timer
        g[u]?.forEach { if (timer_storage[it.first] == 0)
                        vertex_timer = max(dfs_with_timer(g, it.first, vertex_timer, timer_storage), vertex_timer) }
        timer_storage[u] = vertex_timer + 1
        return vertex_timer + 1
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

    fun dfs_to_build_components(g: Map<Vertex, Set<Pair<Vertex, Double>>>, u: Vertex, component: MutableSet<Vertex>) {
        g[u]?.forEach { component.add(it.first); dfs_to_build_components(g, it.first, component) }
    }

    val source_graph_timer_storage = this.vertices.associateWith { 0 }.toMutableMap()
    val source_graph = this.get_adjacency_list()
    var source_graph_timer = 1
    this.vertices.forEach { vertex -> if (source_graph_timer_storage[vertex] == 0) source_graph_timer =
                            dfs_with_timer(source_graph, vertex, source_graph_timer, source_graph_timer_storage) }


    val traversable_vertices = source_graph_timer_storage.keys.toSortedSet(compareBy
                                                { source_graph_timer_storage[it] })
    val inverted_graph = invert_edges(this).get_adjacency_list()
    val strongly_connected_components = mutableSetOf<MutableSet<Vertex>>()
    while (!traversable_vertices.isEmpty()) {
        val another_vertex = traversable_vertices.last()
        val new_component = mutableSetOf<Vertex>(another_vertex)
        dfs_to_build_components(inverted_graph, another_vertex, new_component)
        new_component.forEach { traversable_vertices.remove(it) }
        strongly_connected_components.add(new_component)
    }

    return strongly_connected_components
}
