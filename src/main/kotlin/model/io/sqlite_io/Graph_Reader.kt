package model.io.sqlite_io

import graph.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class Graph_Reader(val path: String) {
    val db_path = "jdbc:sqlite:" + this.path

    private fun connect(): Connection? {
        return try {
            DriverManager.getConnection(this.db_path)
        }
        catch (e: SQLException) {
            null
        }
    }

    public fun read_graph(): Graph? {
        return try {
            this.connect()?.use {
                conn ->
                read_properties(conn)
            }
        }
        catch (e: Exception) {
            return null
        }
    }

    private fun read_properties(conn: Connection): Graph? {
        try {
            conn.createStatement().use {
                statement ->
                val query = "SELECT directed, weighted FROM properties"
                val data = statement.executeQuery(query)
                if (!data.next()) {
                    return null
                }
                val graph: Graph = Graph(data.getInt("directed") == 1,
                    data.getInt("weighted") == 1)
                val successfully_read = read_vertices_and_edges(conn, graph)
                return if (successfully_read) graph else null
            }
        }
        catch (e: Exception) {
            return null
        }
    }

    private fun read_vertices_and_edges(conn: Connection, graph: Graph): Boolean {
        val numeration = read_vertices(conn, graph) ?: return false
        return read_edges(conn, numeration, graph)
    }

    private fun read_vertices(conn: Connection, graph: Graph): Map<Int, Vertex>? {
        try {
            conn.createStatement().use {
                statement ->
                val query = "SELECT vertex, x, y FROM vertices"
                val data = statement.executeQuery(query)
                val numeration = mutableMapOf<Int, Vertex>()
                while (data.next()) {
                    val id = data.getInt("vertex")
                    numeration.put(id, Vertex(data.getDouble("x"), data.getDouble("y")))
                    graph.add_vertex(numeration[id]!!)
                }
                return numeration.toMap()
            }
        }
        catch (e: Exception) {
            return null
        }
    }

    private fun read_edges(conn: Connection, numeration: Map<Int, Vertex>, graph: Graph): Boolean {
        return try {
            conn.createStatement().use {
                statement ->
                val query = "SELECT from_vertex, to_vertex, weight FROM edges"
                val data = statement.executeQuery(query)
                while (data.next()) {
                    val from = numeration[data.getInt("from_vertex")] ?: return false
                    val to = numeration[data.getInt(("to_vertex"))] ?: return false
                    val weight = if (graph.weighted) data.getDouble("weight") else 1.0
                    if (!graph.add_edge(from, to, weight)) return false
                }
                true
            }
        }
        catch (e: Exception) {
            false
        }
    }
}
