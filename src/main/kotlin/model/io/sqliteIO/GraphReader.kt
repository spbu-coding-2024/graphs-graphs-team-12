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
            println(e)
            null
        }

    fun readGraph(): Graph? {
        return try {
            this.connect()?.use { conn ->
                readProperties(conn)
            }
        } catch (e: Exception) {
            println(e)
            return null
        }
    }

    private fun readProperties(conn: Connection): Graph? {
        try {
            conn.createStatement().use { statement ->
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
                return if (successfullyRead) graph else null
            }
        } catch (e: Exception) {
            println(e)
            return null
        }
    }

    private fun readVerticesAndEdges(
        conn: Connection,
        graph: Graph,
    ): Boolean {
        val numeration = readVertices(conn, graph) ?: return false
        return readEdges(conn, numeration, graph)
    }

    private fun readVertices(
        conn: Connection,
        graph: Graph,
    ): Map<Int, Vertex>? {
        try {
            conn.createStatement().use { statement ->
                val query = "SELECT vertex, x, y FROM vertices"
                val data = statement.executeQuery(query)
                val numeration = mutableMapOf<Int, Vertex>()
                while (data.next()) {
                    val id = data.getInt("vertex")
                    numeration.put(id, Vertex(data.getDouble("x"), data.getDouble("y")))
                    graph.addVertex(numeration[id]!!)
                }
                return numeration.toMap()
            }
        } catch (e: Exception) {
            println(e)
            return null
        }
    }

    private fun readEdges(
        conn: Connection,
        numeration: Map<Int, Vertex>,
        graph: Graph,
    ): Boolean {
        return try {
            conn.createStatement().use { statement ->
                val query = "SELECT from_vertex, to_vertex, weight FROM edges"
                val data = statement.executeQuery(query)
                while (data.next()) {
                    val from = numeration[data.getInt("from_vertex")] ?: return false
                    val to = numeration[data.getInt(("to_vertex"))] ?: return false
                    val weight = if (graph.weighted) data.getDouble("weight") else 1.0
                    if (!graph.addEdge(from, to, weight)) return false
                }
                true
            }
        } catch (e: Exception) {
            println(e)
            false
        }
    }
}
