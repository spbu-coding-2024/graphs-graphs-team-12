package model.algo

import graph.Graph
import graph.Vertex
import java.util.SortedSet
import kotlin.math.max

fun Graph.findSCC(): MutableSet<MutableSet<Vertex>>? {
    if (!this.directed) {
        return null
    }

    fun dfsWithTimer(
        g: Map<Vertex, Set<Pair<Vertex, Double>>>,
        u: Vertex,
        timer: Int,
        timerStorage: MutableMap<Vertex, Int>,
    ): Int {
        var vertexTimer = timer
        timerStorage[u] = -1
        g[u]?.forEach {
            if (timerStorage[it.first] == 0) {
                vertexTimer = max(dfsWithTimer(g, it.first, vertexTimer, timerStorage), vertexTimer)
            }
        }
        timerStorage[u] = vertexTimer + 1
        return vertexTimer + 1
    }

    fun invertEdges(g: Graph): Graph {
        val inverted = Graph(g.directed, g.weighted)
        g.vertices.forEach { vertex -> inverted.addVertex(vertex) }
        val gAdjacencyList = g.getAdjacencyList()
        gAdjacencyList.keys.forEach { vertex ->
            gAdjacencyList[vertex]?.forEach { edge ->
                inverted.addEdge(edge.first, vertex, edge.second)
            }
        }
        return inverted
    }

    fun dfsToBuildComponents(
        g: Map<Vertex, Set<Pair<Vertex, Double>>>,
        u: Vertex,
        component: MutableSet<Vertex>,
        canVisit: SortedSet<Vertex>,
    ) {
        g[u]?.forEach {
            if (it.first in canVisit && it.first !in component) {
                component.add(it.first)
                dfsToBuildComponents(g, it.first, component, canVisit)
            }
        }
    }

    val sourceGraphTimerStorage = this.vertices.associateWith { 0 }.toMutableMap()
    val sourceGraph = this.getAdjacencyList()
    var sourceGraphTimer = 1
    this.vertices.forEach { vertex ->
        if (sourceGraphTimerStorage[vertex] == 0) {
            sourceGraphTimer =
                dfsWithTimer(sourceGraph, vertex, sourceGraphTimer, sourceGraphTimerStorage)
        }
    }

    val traversableVertices =
        sourceGraphTimerStorage.keys
            .toSortedSet(
                compareBy
                    { sourceGraphTimerStorage[it] },
            )
    val invertedGraph = invertEdges(this).getAdjacencyList()
    val stronglyConnectedComponents = mutableSetOf<MutableSet<Vertex>>()
    val visitedVertices = this.vertices.associateWith { false }.toMutableMap()
    while (!traversableVertices.isEmpty()) {
        val anotherVertex = traversableVertices.last()
        val newComponent = mutableSetOf<Vertex>(anotherVertex)
        dfsToBuildComponents(invertedGraph, anotherVertex, newComponent, traversableVertices)
        newComponent.forEach { traversableVertices.remove(it) }
        stronglyConnectedComponents.add(newComponent)
    }

    return stronglyConnectedComponents
}
