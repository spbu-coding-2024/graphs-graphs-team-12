package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import graph.Graph
import model.algo.louvain
import model.io.sqliteIO.GraphReader

enum class GwButtonType {
    COMMUNITIES,
    MST,
    SCC,
    SQLITELOAD,
}

class GraphVM(
    val readFrom: String,
    val xMax: MutableState<Float>,
    val yMax: MutableState<Float>,
) {
    val graph: Graph = this.read()
    val v = graph.vertices.associateWith { v -> VertexVM(v, xMax, yMax) }
    val e =
        graph.edges
            .flatMap { e -> e.value }
            .associateWith { e ->
                EdgeVM(v[e.from]!!, v[e.to]!!, graph.directed)
            }

    fun read(): Graph {
        val reader = GraphReader(this.readFrom)
        val g = reader.readGraph()
        println(g)
        return g ?: throw Exception("Не удалось прочитать граф")
    }

    fun execute(action: GwButtonType) {
        when (action) {
            GwButtonType.COMMUNITIES -> communities()
            GwButtonType.MST -> mst()
            GwButtonType.SCC -> scc()
            GwButtonType.SQLITELOAD -> load()
        }
    }

    fun communities() {
        val colors =
            listOf(
                Color.Blue,
                Color.Red,
                Color.Green,
                Color.Yellow,
                Color.White,
                Color.Cyan,
            )
        val communities = graph.louvain()
        communities.keys.forEach { vert -> this.v[vert]?.color?.value = colors[communities[vert]!!] }
    }

    fun mst() {
        TODO()
    }

    fun scc() {
        TODO()
    }

    fun load() {
        TODO()
    }
}
