package model.algo

import graph.Graph
import graph.Vertex
import org.gephi.graph.api.GraphModel
import org.gephi.statistics.plugin.Modularity

fun Graph.louvain(): Map<Vertex, Int> {
    val graphModel = GraphModel.Factory.newInstance()

    val myVertToGephi =
        this.vertices.associateWith { vertex -> graphModel.factory().newNode() }

    val listOfMyEdges =
        this.edges.values.flatten()
    var edgeId = -1
    val myEdgesToGephi =
        listOfMyEdges.associateWith { edge ->
            edgeId += 1
            graphModel.factory().newEdge(
                myVertToGephi[edge.from],
                myVertToGephi[edge.to],
                edgeId,
                edge.weight,
                this.directed,
            )
        }

    val g = graphModel.getDirectedGraph()
    myVertToGephi.values.forEach { g.addNode(it) }
    myEdgesToGephi.values.forEach { g.addEdge(it) }

    Modularity().execute(g)

    return this.vertices.associateWith { vertex ->
        myVertToGephi[vertex]?.getAttribute(Modularity.MODULARITY_CLASS) as? Int ?: -1
    }
}
