package graph

class Graph(
    val directed: Boolean,
    val weighted: Boolean,
) {
    internal val vertices: MutableSet<Vertex> = mutableSetOf()
    internal val edges: MutableMap<Vertex, MutableSet<Edge>> = mutableMapOf()

    internal fun addVertex(v: Vertex) {
        vertices.add(v)
    }

    internal fun addEdge(
        u: Vertex,
        v: Vertex,
        w: Double = 1.0,
    ): Boolean {
        if (!this.directed && edges[u]?.any { it.to == v && it.weight == w } ?: false) {
            return true
        }
        if (u == v) {
            return false
        }
        if (!this.weighted && w != 1.0) {
            return false
        }
        if ((edges[u])?.any { it.to == v } == true) {
            return false
        }
        edges[u]?.add(Edge(u, v, w)) ?: edges.put(u, mutableSetOf(Edge(u, v, w)))
        if (!this.directed) {
            edges[v]?.add(Edge(v, u, w)) ?: edges.put(v, mutableSetOf(Edge(v, u, w)))
        }
        return true
    }

    internal fun getAdjacencyList(): Map<Vertex, Set<Pair<Vertex, Double>>> {
        val edgeToTargetVertex: (Edge) -> Pair<Vertex, Double> = { e: Edge -> Pair(e.to, e.weight) }
        // можно использовать !!, так как изначально используем ключи из MutableMap
        return edges.keys.associateBy({ vertex -> vertex }, { vertex ->
            edges[vertex]!!.map { edge -> edgeToTargetVertex(edge) }.toSet()
        })
    }

    internal fun getAdjacencyMatrix(): Map<Vertex, MutableMap<Vertex, Double?>> {
        val adjMatrix: Map<Vertex, MutableMap<Vertex, Double?>> =
            vertices.associateBy({ vertex -> vertex }, { vertex ->
                vertices.associateBy({ vertex -> vertex }, { null }).toMutableMap()
            })
        // можно использовать !!, так как точно знаем, что каждой вершине соответствует MutableMap
        edges.keys.forEach { vertex -> edges[vertex]!!.forEach { adjMatrix[vertex]!![it.to] = it.weight } }
        return adjMatrix
    }
}
