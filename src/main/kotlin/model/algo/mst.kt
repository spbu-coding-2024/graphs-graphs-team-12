package model.algo
import graph.*

fun Graph.build_mst(): Graph? {
    // проверка - граф неориентированный, взвешенный, связный
    if (this.directed) {
        return null
    }

    if (!this.weighted) {
        return null
    }


    fun check_connectivity(g: Map<Vertex, MutableMap<Vertex, Double?>>): Boolean {
        val visited = mutableSetOf<Vertex>()

        fun dfs(v: Vertex, g: Map<Vertex, MutableMap<Vertex, Double?>>,
                visited: MutableSet<Vertex>) {
            visited.add(v)
            g[v]?.keys?.forEach { vertex -> if (g[v]!![vertex] != null &&
                                  vertex !in visited)
                                  dfs(vertex, g, visited) }
        }

        dfs(g.keys.toList()[0], g, visited)
        if (visited == this.vertices) return true else return false
    }


    fun update_weights(not_in_tree: MutableSet<Vertex>, g: Map<Vertex, MutableMap<Vertex, Double?>>,
                       mst: Graph): MutableSet<Vertex> {
        val updated_set = not_in_tree.map { vertex -> g[vertex]!!.
        filter { it.key in mst.vertices }.
        map { Triple(vertex, it.key, it.value ?: Double.POSITIVE_INFINITY) }.
        minBy { it.third }
        }

        val to_add = updated_set.minBy { it.third }
        mst.add_vertex(to_add.first)
        mst.add_edge(to_add.first, to_add.second, to_add.third)
        not_in_tree.remove(to_add.first)

        return not_in_tree
    }


    val graph = this.get_adjacency_matrix()
    if (!check_connectivity(graph)) return null

    val mstree = Graph(false, true)
    val first_vertex = this.vertices.toList()[0]
    mstree.add_vertex(first_vertex)
    var not_in_tree: MutableSet<Vertex> = this.vertices.
    filter { vertex -> vertex !in mstree.vertices }.
    toMutableSet()
    var to_add: Triple<Vertex, Vertex, Double>

    repeat(this.vertices.size - 1) {
        not_in_tree = update_weights(not_in_tree, graph, mstree)
    }

    return mstree
}
