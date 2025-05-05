package model.io.sqlite_io

import graph.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class Graph_Loader(val graph: Graph, val path: String) {
    private val db_path: String = "jdbc:sqlite:" + this.path

    private fun connect(): Connection? {
        try {
            return DriverManager.getConnection(this.db_path)
        }
        catch (e: SQLException) {
            return null
        }
    }

    public fun load_graph(): Boolean {
        return this.connect()?.use {
            conn ->
            return load_properties(conn) && load_vertices_and_edges(conn)
        } ?: false
    }

    private fun load_properties(conn: Connection): Boolean {
        try {
            conn.createStatement().use {
                statement -> val query = "CREATE TABLE properties " +
                    "(directed INTEGER weighted INTEGER)"
                statement.execute(query)
            }

            val insert_query = "INSERT INTO properties(directed, weighted) VALUES(?, ?)"
            conn.prepareStatement(insert_query).use {
                prep_statement ->
                prep_statement.setInt(1, if (this.graph.directed) 1 else 0)
                prep_statement.setInt(2, if (this.graph.weighted) 1 else 0)
                prep_statement.executeUpdate()
            }
            return true
        }
        catch (e: Exception) {
            return false
        }
    }

    private fun load_vertices_and_edges(conn: Connection): Boolean {
        val indexation = load_vertices(conn) ?: return false
        return load_edges(conn, indexation)
    }

    private fun load_vertices(conn: Connection): Map<Vertex, Int>?{
        try {
            conn.createStatement().use {
                statement -> val query = "CREATE TABLE vertices " +
                    "(vertex INTEGER PRIMARY KEY)"
                statement.execute(query)
            }

            val insert_query = "INSERT INTO vertices(vertex) VALUES(?)"
            conn.prepareStatement(insert_query).use {
                prep_statement ->
                var index = -1
                val indexation: Map<Vertex, Int>
                indexation = this.graph.vertices.associate { vertex -> index += 1; vertex to index}
                indexation.keys.forEach { vertex -> prep_statement.setInt(1, indexation[vertex]!!);
                    prep_statement.addBatch()}
                prep_statement.executeBatch()
                return indexation
            }
        }
        catch (e: Exception) {
            return null
        }
    }

    private fun load_edges(conn: Connection, indexation: Map<Vertex, Int>): Boolean {
        try {
            conn.createStatement().use {
                statement ->
                val query = "CREATE TABLE edges " +
                        "(from INTEGER to INTEGER weight DOUBLE)"
                statement.execute(query)
            }

            val insert_query = "INSERT INTO edges(from, to, weight) VALUES(?, ?, ?)"
            val prep_statement = conn.prepareStatement(insert_query).use {
                prep_statement ->
                this.graph.edges.keys.forEach { vertex ->
                    graph.edges[vertex]!!.forEach {
                        prep_statement.setInt(1, indexation[it.from]!!);
                        prep_statement.setInt(2, indexation[it.to]!!);
                        prep_statement.setDouble(3, it.weight);
                        prep_statement.addBatch()
                    }
                }
            }
            return true
        }
        catch (e: Exception) {
            return false
        }
    }
}
