package viewmodel

import androidx.compose.ui.graphics.drawscope.DrawScope
import graph.Graph

enum class GwButtonType {
    COMMUNITIES,
    MST,
    SCC,
    SQLITELOAD,
}

class GraphVM(
    val readFrom: String,
    val scope: DrawScope
) {
    // вводится из бдшки
    val g: Graph = Graph(true, true)
    val v = g.vertices.associateWith { v -> VertexVM(v) }
    val listE = g.edges.flatMap { e -> e.value }
    val e = listE.associateWith { e ->
        EdgeVM(v[e.from]!!, v[e.to]!!, scope) }

    fun execute(action: GwButtonType) {
        when (action) {
            GwButtonType.COMMUNITIES -> communities()
            GwButtonType.MST -> mst()
            GwButtonType.SCC -> scc()
            GwButtonType.SQLITELOAD -> load()
        }
    }

    fun communities() {
        TODO()
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
