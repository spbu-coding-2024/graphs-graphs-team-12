package graph

class Graph(val directed: Boolean, val weighted: Boolean, val multigraph: Boolean) {
    internal val vertices: MutableSet<Vertex> = mutableSetOf()
    internal val edges: MutableMap<Vertex, MutableSet<Edge>> = mutableMapOf()

    internal fun add_vertex(v: Vertex) {
        vertices.add(v)
    }

    internal fun add_edge(u: Vertex, v: Vertex, w: Double = 1.0) {
        if (!this.multigraph && ((edges[u])?.any { it.from == u && it.to == v } == true)) {
            throw Exception("Such edge between vertices already exists")
        }
        edges[u]?.add(Edge(u, v, w)) ?: edges.put(u, mutableSetOf(Edge(u, v, w)))
        if (!this.directed) {
            edges[v]?.add(Edge(v, u, w)) ?: edges.put(u, mutableSetOf(Edge(v, u, w)))
        }
    }

    internal fun get_adjacency_list(): Map<Vertex, Set<Pair<Vertex, Double>>> {
        if (this.multigraph) {
            TODO()
        }
        val edge_to_target_vertex: (Edge) -> Pair<Vertex, Double> = { e: Edge -> Pair(e.to, e.weight) }
        // можно использовать !!, так как изначально используем ключи из MutableMap
        return edges.keys.associateBy({vertex -> vertex }, { vertex ->
               edges[vertex]!!.map { edge -> edge_to_target_vertex(edge) }.toSet() })
    }

    internal fun get_adjacency_matrix(): Map<Vertex, MutableMap<Vertex, Double?>> {
        val adj_matrix: Map<Vertex, MutableMap<Vertex, Double?>> = vertices.associateBy({ vertex -> vertex }, { vertex ->
                                                   vertices.associateBy({ vertex -> vertex }, { null }).toMutableMap() })
        // можно использовать !!, так как точно знаем, что каждой вершине соответствует MutableMap
        edges.keys.forEach { vertex -> edges[vertex]!!.forEach { adj_matrix[vertex]!![it.to] = it.weight } }
        return adj_matrix
    }
}
