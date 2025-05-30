package model.io.sqliteIO

import graph.Graph
import graph.Vertex
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class GraphReader(
    val path: String,
) {
    val dbPath = "jdbc:sqlite:" + this.path

    private fun connect(): Connection? =
        try {
            DriverManager.getConnection(this.dbPath)
        } catch (e: SQLException) {
            println("hell")
            println(e)
            null
        }

    fun readGraph(): Graph? {
        return try {
            this.connect()?.use { conn ->
                println("did this step")
                readProperties(conn)
            }
        } catch (e: Exception) {
            println("hell here")
            println(e)
            return null
        }
    }

    private fun readProperties(conn: Connection): Graph? {
        try {
            conn.createStatement().use { statement ->
                println("doing reading of properties")
                val query = "SELECT directed, weighted FROM properties"
                val data = statement.executeQuery(query)
                if (!data.next()) {
                    return null
                }
                val graph: Graph =
                    Graph(
                        data.getInt("directed") == 1,
                        data.getInt("weighted") == 1,
                    )
                val successfullyRead = readVerticesAndEdges(conn, graph)
                println("read edges too")
                println(successfullyRead)
                return if (successfullyRead) graph else null
            }
        } catch (e: Exception) {
            println("tilt here")
            println(e)
            return null
        }
    }

    private fun readVerticesAndEdges(
        conn: Connection,
        graph: Graph,
    ): Boolean {
        val numeration = readVertices(conn, graph) ?: false
        if (numeration == false) {
            println("AAAAAA")
            return false
        }
        println("did read vertices")
        return readEdges(conn, numeration as Map<Int, Vertex>, graph)
    }

    private fun readVertices(
        conn: Connection,
        graph: Graph,
    ): Map<Int, Vertex>? {
        try {
            conn.createStatement().use { statement ->
                val query = "SELECT vertex, x, y, name FROM vertices"
                val data = statement.executeQuery(query)
                val numeration = mutableMapOf<Int, Vertex>()
                while (data.next()) {
                    val id = data.getInt("vertex")
                    println(data.getString("name"))
                    numeration.put(id, Vertex(data.getDouble("x"), data.getDouble("y"), data.getString("name")))
                    graph.addVertex(numeration[id]!!)
                }
                return numeration.toMap()
            }
        } catch (e: Exception) {
            println("died here")
            println(e)
            return null
        }
    }

    private fun readEdges(
        conn: Connection,
        numeration: Map<Int, Vertex>,
        graph: Graph,
    ): Boolean =
        try {
            conn.createStatement().use { statement ->
                val query = "SELECT from_vertex, to_vertex, weight FROM edges"
                val data = statement.executeQuery(query)
                println(numeration)
                while (data.next()) {
                    val from = numeration[data.getInt("from_vertex")] ?: println("wtffff")
                    val to = numeration[data.getInt(("to_vertex"))] ?: println("how")
                    val weight = if (graph.weighted) data.getDouble("weight") else 1.0
                    if (!graph.addEdge(from as Vertex, to as Vertex, weight)) println("shiiiiit")
                    println("im here")
                }
                println("did it all lol wtf")
                true
            }
            true
        } catch (e: Exception) {
            println("fell here")
            println(e)
            false
        }
}
