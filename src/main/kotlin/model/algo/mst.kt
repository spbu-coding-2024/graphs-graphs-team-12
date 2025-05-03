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
            g[v]?.keys?.forEach { vertex -> if (g[v]!![vertex] != null)
                                  dfs(vertex, g, visited) }
        }

        dfs(g.keys.toList()[0], g, visited)
        if (visited == this.vertices) return true else return false
    }

    val graph = this.get_adjacency_matrix()
    if (!check_connectivity(graph)) return null

    val mstree = Graph(true, true)
    val first_vertex = this.vertices.toList()[0]
    mstree.add_vertex(first_vertex)
    val not_in_tree: MutableSet<Triple<Vertex, Vertex, Double?>> = this.vertices.
    filter { vertex -> vertex !in mstree.vertices }.
    map { vertex -> Triple(vertex, first_vertex, graph[vertex]!![first_vertex]) }.
    toMutableSet()
    var to_add: Triple<Vertex, Vertex, Double?>

    repeat(this.vertices.size - 1) {
        to_add = not_in_tree.filter { it.third != null }.minBy { it.third ?: Double.MAX_VALUE }

        mstree.add_vertex(to_add.first)
        mstree.add_edge(to_add.first, to_add.second, to_add.third ?: throw Exception("Something wrong"))

        not_in_tree.remove(to_add)
        not_in_tree.map { triple -> graph[triple.first]!!.
                          filter { it.key in mstree.vertices }.
                          map { it -> Triple(triple.first, it.key,
                                graph[triple.first]!![it.key]) }.
                          minBy { triple.third ?: Double.MAX_VALUE }
        }
    }

    return mstree
}
