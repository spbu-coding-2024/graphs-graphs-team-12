package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import graph.Graph
import kotlinx.coroutines.delay
import model.algo.buildMST
import model.algo.findSCC
import model.algo.louvain
import model.io.sqliteIO.GraphLoader
import model.io.sqliteIO.GraphReader
import java.io.File

suspend fun waitUntil(condition: MutableState<Boolean>) {
    while (!condition.value) {
        delay(50)
    }
}

enum class GwButtonType {
    COMMUNITIES,
    MST,
    SCC,
    SQLITELOAD,
}

class GraphVM(
    val readFrom: String,
    val extension: String,
    val error: MutableState<Boolean>,
    val errorMessage: MutableState<String>,
    val askInput: MutableState<Boolean>,
    val loadFile: MutableState<String>,
) {
    val graph: Graph = this.read()
    val v = graph.vertices.associateWith { v -> VertexVM(v) }
    val e =
        graph.edges
            .flatMap { e -> e.value }
            .associateWith { e ->
                EdgeVM(v[e.from]!!, v[e.to]!!, graph.directed)
            }
    val colors =
        listOf(
            Color.Blue,
            Color.Red,
            Color.Green,
            Color.Yellow,
            Color.White,
            Color.Cyan,
        )

    fun callError(message: String) {
        error.value = true
        errorMessage.value = message
    }

    fun read(): Graph {
        when (this.extension) {
            "db" -> return this.readSQL()
            else -> TODO()
        }
    }

    fun readSQL(): Graph {
        val reader = GraphReader(this.readFrom)
        val g = reader.readGraph()
        if (g == null) {
            callError("Не удалось прочитать граф")
        }
        return g ?: Graph(false, false)
    }

    fun execute(action: GwButtonType) {
        when (action) {
            GwButtonType.COMMUNITIES -> communities()
            GwButtonType.MST -> mst()
            GwButtonType.SCC -> scc()
            GwButtonType.SQLITELOAD -> load(GwButtonType.SQLITELOAD)
        }
    }

    fun communities() {
        val communities = graph.louvain()
        communities.keys.forEach { vert ->
            this.v[vert]?.color?.value =
                colors[
                    communities[vert]
                        ?: return callError("Ошибка при выполнении алгоритма"),
                ]
        }
    }

    fun mst() {
        val edges = graph.buildMST()
        (edges ?: return callError("Невозможно найти минимальный остов этого графа")).forEach {
            (e[it] ?: return callError("Ошибка при выполнении алгоритма")).color.value =
                Color.Red
        }
    }

    fun scc() {
        val res =
            (
                graph.findSCC() ?: return
                    callError("Невозможно найти компоненты сильной связности этого графа")
            ).toList()
        res.forEachIndexed { index, vertices ->
            vertices.forEach { vertex ->
                (
                    v[vertex] ?: return callError(
                        "Ошибка при выполнении алгоритма",
                    )
                ).color.value = colors[index]
            }
        }
    }

    fun load(db: GwButtonType) {
        val file = File(loadFile.value)
        if (db == GwButtonType.SQLITELOAD) {
            if (file.path.split(".").last() != "db") {
                return callError("Неверное расширение")
            }
            file.createNewFile()
            if (file.length() != 0L) {
                return callError("Невозможно записать граф - файл непустой")
            }
            val loader = GraphLoader(graph, file.path)
            if (!loader.loadGraph()) {
                return callError("Не получилось записать граф")
            } else {
                return callError("Граф успешно записан")
            }
        } else {
            return callError("В разработке:)")
        }
    }
}
