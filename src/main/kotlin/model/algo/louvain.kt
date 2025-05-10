package model.algo

import graph.*
import org.gephi.graph.api.GraphModel
import org.gephi.statistics.api.*
import org.gephi.statistics.spi.*
import org.gephi.statistics.plugin.Modularity

fun Graph.louvain(): Map<Vertex, Int> {
    val graph_model = GraphModel.Factory.newInstance()

    val my_vert_to_gephi =
            this.vertices.associateWith { vertex -> graph_model.factory().newNode() }

    val list_of_my_edges =
        this.edges.values.flatten()
    var edge_id = -1
    val my_edges_to_gephi = list_of_my_edges.associateWith { edge ->
        edge_id += 1;
        graph_model.factory().newEdge(my_vert_to_gephi[edge.from], my_vert_to_gephi[edge.to], edge_id,
        edge.weight, this.directed) }

    val g = graph_model.getDirectedGraph()
    my_vert_to_gephi.values.forEach { g.addNode(it) }
    my_edges_to_gephi.values.forEach { g.addEdge(it) }

    Modularity().execute(g)

    return this.vertices.associateWith { vertex ->
        my_vert_to_gephi[vertex]?.getAttribute(Modularity.MODULARITY_CLASS) as? Int ?: -1 }
}
