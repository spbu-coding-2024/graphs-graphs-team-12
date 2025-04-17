package graph

class Graph(val directed: Boolean, val weighted: Boolean) {
    internal val vertices: MutableSet<Vertex> = mutableSetOf()
    internal val edges: MutableSet<Edge> = mutableSetOf()

    internal fun add_vertex(v: Vertex) {
        TODO()
    }

    internal fun remove_vertex(v: Vertex) {
        TODO()
    }

    internal fun add_edge(e: Edge) {
        TODO()
    }

    internal fun remove_edge(e: Edge) {
        TODO()
    }
}
