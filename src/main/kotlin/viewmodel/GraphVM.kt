package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import graph.Graph
import graph.Vertex
import kotlinx.coroutines.delay
import model.algo.applyForceAtlas2
import model.algo.bellman
import model.algo.buildMST
import model.algo.dijkstra
import model.algo.findBridges
import model.algo.findCycles
import model.algo.findSCC
import model.algo.getHarmonicCentrality
import model.algo.louvain
import model.io.json.JsonGraphIO
import model.io.neo4jIO.Neo4jRepository
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
    DIJKSTRA,
    CYCLES,
    HARMONIC,
    SQLITELOAD,
    NEO4JLOAD,
    EDGELABELS,
    VERTICESLABELS,
    CLEAN,
    JSON_SAVE,
    BRIDGES,
    BELLMAN_FORD,
    LAYOUT,
}

class GraphVM(
    val readFrom: String,
    val extension: String,
    val error: MutableState<Boolean>,
    val errorMessage: MutableState<String>,
    val askInput: MutableState<Boolean>,
    val loadFile: MutableState<String>,
    val showResult: MutableState<Boolean>,
    val resultMessage: MutableState<String>,
    val result: MutableState<Double>,
) {
    val graph: Graph = this.read()
    val v = graph.vertices.associateWith { v -> VertexVM(v) }
    val selected = mutableListOf<VertexVM>()
    val isStrongGravity = mutableStateOf(false)
    val neo4jOpen = mutableStateOf(false)
    var pendingAction: GwButtonType = GwButtonType.SQLITELOAD
    val neo4jUri = mutableStateOf("")
    val neo4jUser = mutableStateOf("")
    val neo4jPassword = mutableStateOf("")

    val e =
        graph.edges
            .flatMap { e -> e.value }
            .associateWith { e ->
                EdgeVM(e, v[e.from]!!, v[e.to]!!, graph.directed)
            }
    val colors =
        listOf(
            Color.Blue,
            Color.Red,
            Color.Green,
            Color.Yellow,
            Color.White,
            Color.Cyan,
            Color.Magenta,
        )

    fun clean() {
        v.values.forEach {
            it.selected.value = false
            it.color.value = Color.Black
            it.radius.value = 10.0
        }
        e.keys.forEach {
            val edgeVM = e[it]
            edgeVM?.color?.value = Color.Black
        }
        selected.clear()
    }

    fun callError(message: String) {
        error.value = true
        errorMessage.value = message
    }

    fun read(): Graph {
        when (this.extension) {
            "db" -> return this.readSQL()
            "neo4j" -> return this.readNeo4j()
            "json" -> return this.readJson()
            else -> return Graph(false, false)
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

    fun readJson(): Graph =
        try {
            val io = JsonGraphIO()
            io.loadGraph(this.readFrom)
        } catch (e: Exception) {
            callError("Ошибка чтения из JSONr: ${e.message}")
            Graph(false, false)
        }

    fun readNeo4j(): Graph =
        try {
            val (uri, user, password) = readFrom.split(";")
            val repo = Neo4jRepository(uri, user, password)
            repo.readNeo4j() ?: Graph(false, false)
        } catch (e: Exception) {
            callError("Ошибка чтения из Neo4j: ${e.message}")
            Graph(false, false)
        }

    fun execute(action: GwButtonType) {
        when (action) {
            GwButtonType.COMMUNITIES -> communities()
            GwButtonType.MST -> mst()
            GwButtonType.SCC -> scc()
            GwButtonType.DIJKSTRA -> dijkstraAlgo()
            GwButtonType.CYCLES -> cycles()
            GwButtonType.HARMONIC -> harmonic()
            GwButtonType.LAYOUT -> layout()
            GwButtonType.BRIDGES -> bridges()
            GwButtonType.BELLMAN_FORD -> bellmanFordAlgo()
            GwButtonType.SQLITELOAD -> load(GwButtonType.SQLITELOAD)
            GwButtonType.NEO4JLOAD -> load(GwButtonType.NEO4JLOAD)
            GwButtonType.JSON_SAVE -> load(GwButtonType.JSON_SAVE)
            GwButtonType.EDGELABELS -> e.values.forEach { it.showLabel.value = !it.showLabel.value }
            GwButtonType.VERTICESLABELS -> v.values.forEach { it.showLabel.value = !it.showLabel.value }
            GwButtonType.CLEAN -> clean()
        }
    }

    fun communities() {
        // clean()
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
        // clean()
        val edges = graph.buildMST()
        (edges ?: return callError("Невозможно найти минимальный остов этого графа")).forEach {
            (e[it] ?: return callError("Ошибка при выполнении алгоритма")).color.value =
                Color.Red
        }
        val inversedEdges =
            edges.map { edge ->
                e.keys.first {
                    it.from == edge.to &&
                        it.to == edge.from &&
                        it.weight == edge.weight
                }
            }
        inversedEdges.forEach {
            (e[it] ?: return callError("Ошибка при выполнении алгоритма")).color.value =
                Color.Red
        }
        showResult.value = true
        resultMessage.value = "Вес минимального остова:"
        result.value = edges.fold(0.0) { sum, edge -> sum + edge.weight }
    }

    fun scc() {
        // clean()
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

    fun bridges() {
        clean()
        try {
            val foundBridges = findBridges(graph)
            if (foundBridges.isEmpty()) {
                callError("Мосты не найдены")
                return
            }
            foundBridges.forEach { bridge ->
                var edgeVM = e[bridge]
                if (edgeVM == null) {
                    val key =
                        e.keys.find {
                            it.from == bridge.from && it.to == bridge.to && it.weight == bridge.weight
                        }
                    if (key != null) edgeVM = e[key]
                }
                if (edgeVM == null) return callError("Ошибка визуализации моста (View не найден)")

                edgeVM.color.value = Color.Red
                if (!graph.directed) {
                    val revKey =
                        e.keys.find {
                            it.from == bridge.to && it.to == bridge.from && it.weight == bridge.weight
                        }
                    if (revKey != null) e[revKey]?.color?.value = Color.Red
                }
            }
            showResult.value = true
            resultMessage.value = "Найдено мостов:"
            result.value = foundBridges.size.toDouble()
        } catch (e: Exception) {
            callError("Ошибка алгоритма мостов: ${e.message}")
        }
    }

    fun bellmanFordAlgo() {
        val selection = v.values.filter { it.selected.value }

        if (selection.size != 2) {
            return callError("Выберите 2 вершины (сейчас выбрано: ${selection.size})")
        }
        val startVM = selection[0]
        val endVM = selection[1]
        clean()
        startVM.selected.value = true
        startVM.color.value = Color.Red
        endVM.selected.value = true
        endVM.color.value = Color.Red

        try {
            val res = bellman(graph, startVM.vertex, endVM.vertex)
            if (res == null) return callError("Путь не найден (или цикл)")
            val (dist, path) = res
            path.forEach { v[it]?.color?.value = Color.Red }
            for (i in 0 until path.size - 1) {
                val u = path[i]
                val w = path[i + 1]
                val edgesToColor =
                    e.keys.filter {
                        (it.from == u && it.to == w) || (it.from == w && it.to == u)
                    }

                edgesToColor.forEach { key ->
                    e[key]?.color?.value = Color.Red
                }
            }

            showResult.value = true
            resultMessage.value = "Кратчайший путь:"
            result.value = dist
        } catch (e: Exception) {
            callError("Ошибка: ${e.message}")
        }
    }

    fun dijkstraAlgo() {
        clean()
        if (selected.size != 2) {
            return callError("Для алгоритма Дейкстры необходимо выбрать 2 вершины")
        }
        val dijkstra = dijkstra(graph, selected[0].vertex, selected[1].vertex)
        if (dijkstra == null) return callError("Алгоритм Дейкстры не работает с отрицательными весами ребер")
        if (dijkstra.first == Double.MAX_VALUE) return callError("Пути не существует")
        val path = dijkstra.second

        path.forEach {
            v[it]?.color?.value = Color.Red
        }

        for (i in 0 until path.size - 1) {
            val from = path[i]
            val to = path[i + 1]
            val edge = e.keys.find { it.from == from && it.to == to || it.from == to && it.to == from }
            val edgeVM = e[edge]
            edgeVM?.color?.value = Color.Red
        }

        showResult.value = true
        resultMessage.value = "Кратчайшее расстояние:"
        result.value = dijkstra.first
    }

    fun cycles() {
        clean()
        if (selected.size != 1) {
            return callError("Выберите 1 вершину для поиска циклов")
        }

        val cycles = findCycles(graph, selected[0].vertex)
        if (cycles.isEmpty()) return callError("Циклы не найдены")

        cycles.forEach { path ->
            path.forEach { v[it]?.color?.value = Color.Red }
            for (i in 0 until path.size - 1) {
                val edge =
                    e.keys.find { it.from == path[i] && it.to == path[i + 1] || it.from == path[i + 1] && it.to == path[i] }
                e[edge]?.color?.value = Color.Red
            }
        }
    }

    fun harmonic() {
        clean()
        val centralityMap = mutableMapOf<Vertex, Double>()
        var maxCentrality = Double.MIN_VALUE

        graph.vertices.forEach {
            val value =
                getHarmonicCentrality(graph, it) ?: return callError("Алгоритм не работает с отрицательными весами")
            centralityMap[it] = value
            if (value > maxCentrality) maxCentrality = value
        }

        for ((vertex, value) in centralityMap) {
            val vm = v[vertex] ?: continue
            val norm = if (maxCentrality == 0.0) 0.0 else value / maxCentrality
            vm.color.value = Color(red = norm.toFloat(), green = 0f, blue = (1f - norm).toFloat().coerceAtLeast(0f))
            vm.radius.value = 10 + 30 * norm
        }
    }

    fun layout() {
        if (graph.vertices.isEmpty()) return callError("Граф пуст")
        try {
            applyForceAtlas2(graph, useStrongGravity = isStrongGravity.value)
            graph.vertices.forEach { vertex ->
                val vm = v[vertex]
                if (vm != null) {
                    vm.xVM.value = vertex.x
                    vm.yVM.value = vertex.y
                }
            }
        } catch (e: Exception) {
            callError("Ошибка раскладки: ${e.message}")
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
        }
        if (db == GwButtonType.NEO4JLOAD) {
            try {
                v.forEach { (vertex, vm) ->
                    vertex.x = vm.xVM.value
                    vertex.y = vm.yVM.value
                }
                val neo4jRepository = Neo4jRepository(neo4jUri.value, neo4jUser.value, neo4jPassword.value)
                neo4jRepository.writeNeo4j(graph)
                callError("Граф успешно записан")
            } catch (e: Exception) {
                callError("Ошибка записи в Neo4j: ${e.message}")
            }
        }
        if (db == GwButtonType.JSON_SAVE) {
            if (loadFile.value.isEmpty()) return callError("Введите имя файла")
            val path = if (loadFile.value.endsWith(".json")) loadFile.value else "${loadFile.value}.json"

            try {
                val io = JsonGraphIO()
                io.saveGraph(graph, path)
                callError("Успешно сохранено: $path") // Уведомление об успехе
            } catch (e: Exception) {
                callError("Ошибка сохранения JSON: ${e.message}")
            }
            return
        }
    }
}
