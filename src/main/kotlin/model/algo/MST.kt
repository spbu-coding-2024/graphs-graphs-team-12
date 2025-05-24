package model.algo

import graph.Edge
import graph.Graph
import graph.Vertex

fun Graph.buildMST(): ArrayList<Edge>? {
    // проверка - граф неориентированный, взвешенный, связный
    if (this.directed) {
        return null
    }

    if (!this.weighted) {
        return null
    }

    fun checkConnectivity(g: Map<Vertex, MutableMap<Vertex, Double?>>): Boolean {
        val visited = mutableSetOf<Vertex>()

        fun dfs(
            v: Vertex,
            g: Map<Vertex, MutableMap<Vertex, Double?>>,
            visited: MutableSet<Vertex>,
        ) {
            visited.add(v)
            g[v]?.keys?.forEach { vertex ->
                if (g[v]!![vertex] != null &&
                    vertex !in visited
                ) {
                    dfs(vertex, g, visited)
                }
            }
        }

        dfs(g.keys.toList()[0], g, visited)
        if (visited == this.vertices) return true else return false
    }

    fun updateWeights(
        notInTree: MutableSet<Vertex>,
        g: Map<Vertex, MutableMap<Vertex, Double?>>,
        mst: Graph,
        treeEdges: ArrayList<Edge>,
    ): MutableSet<Vertex> {
        val updatedSet =
            notInTree.map { vertex ->
                g[vertex]!!
                    .filter { it.key in mst.vertices }
                    .map { Triple(vertex, it.key, it.value ?: Double.POSITIVE_INFINITY) }
                    .minBy { it.third }
            }

        val toAdd = updatedSet.minBy { it.third }
        mst.addVertex(toAdd.first)
        mst.addEdge(toAdd.first, toAdd.second, toAdd.third)
        treeEdges.add(
            this.edges[toAdd.first]?.first { it.to == toAdd.second && it.weight == toAdd.third }
                ?: throw Exception("Something went really wrong"),
        )
        notInTree.remove(toAdd.first)

        return notInTree
    }

    val graph = this.getAdjacencyMatrix()
    if (!checkConnectivity(graph)) return null

    val mstree = Graph(false, true)
    val firstVertex = this.vertices.toList()[0]
    mstree.addVertex(firstVertex)
    var notInTree: MutableSet<Vertex> =
        this.vertices
            .filter { vertex -> vertex !in mstree.vertices }
            .toMutableSet()
    val mstEdges = arrayListOf<Edge>()

    repeat(this.vertices.size - 1) {
        notInTree = updateWeights(notInTree, graph, mstree, mstEdges)
    }

    return mstEdges
}
