package model.io.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import graph.Graph
import graph.Vertex
import java.io.File

data class JsonGraphData(
    val isDirected: Boolean = false,
    val isWeighted: Boolean = false,
    val vertices: List<JsonVertex>,
    val edges: List<JsonEdge>,
)

data class JsonVertex(
    val id: String,
    val x: Double,
    val y: Double,
)

data class JsonEdge(
    val from: String,
    val to: String,
    val weight: Double,
)

class JsonGraphIO {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    fun loadGraph(filePath: String): Graph {
        val file = File(filePath)
        if (!file.exists()) throw Exception("Файл не найден")

        val jsonString = file.readText()
        val data =
            gson.fromJson(jsonString, JsonGraphData::class.java)
                ?: throw Exception("Неверный формат JSON")
        val graph =
            Graph(
                directed = data.isDirected,
                weighted = data.isWeighted,
            )
        val idToVertex = mutableMapOf<String, Vertex>()
        for (json in data.vertices) {
            val realVertex = Vertex(x = json.x, y = json.y, name = json.id)
            graph.addVertex(realVertex)
            idToVertex[json.id] = realVertex
        }
        for (json in data.edges) {
            val u = idToVertex[json.from]
            val v = idToVertex[json.to]
            if (u != null && v != null) {
                graph.addEdge(u, v, json.weight)
            }
        }
        return graph
    }

    fun saveGraph(
        graph: Graph,
        filePath: String,
    ) {
        val jsonVertices =
            graph.vertices.map { v ->
                JsonVertex(id = v.name, x = v.x, y = v.y)
            }
        val allEdges =
            graph.edges.values.flatten().map { e ->
                JsonEdge(from = e.from.name, to = e.to.name, weight = e.weight)
            }
        val data =
            JsonGraphData(
                isDirected = graph.directed,
                isWeighted = graph.weighted,
                vertices = jsonVertices,
                edges = allEdges,
            )
        val jsonString = gson.toJson(data)
        File(filePath).writeText(jsonString)
    }
}
