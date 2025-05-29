package model.io.sqliteIO
import graph.Graph
import graph.Vertex
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class GraphLoader(
    val graph: Graph,
    val path: String,
) {
    private val dbPath: String = "jdbc:sqlite:" + this.path

    private fun connect(): Connection? =
        try {
            DriverManager.getConnection(this.dbPath)
        } catch (e: SQLException) {
            println(e)
            null
        }

    fun loadGraph(): Boolean {
        return this.connect()?.use { conn ->
            return loadProperties(conn) && loadVerticesAndEdges(conn)
        } ?: false
    }

    private fun loadProperties(conn: Connection): Boolean {
        try {
            conn.createStatement().use { statement ->
                val query =
                    "CREATE TABLE properties " +
                        "(directed INTEGER, weighted INTEGER)"
                statement.execute(query)
            }

            val insertQuery = "INSERT INTO properties(directed, weighted) VALUES(?, ?)"
            conn.prepareStatement(insertQuery).use { prepStatement ->
                prepStatement.setInt(1, if (this.graph.directed) 1 else 0)
                prepStatement.setInt(2, if (this.graph.weighted) 1 else 0)
                prepStatement.executeUpdate()
            }
            return true
        } catch (e: Exception) {
            println(e)
            return false
        }
    }

    private fun loadVerticesAndEdges(conn: Connection): Boolean {
        val indexation = loadVertices(conn) ?: return false
        return loadEdges(conn, indexation)
    }

    private fun loadVertices(conn: Connection): Map<Vertex, Int>? {
        try {
            conn.createStatement().use { statement ->
                val query =
                    "CREATE TABLE vertices " +
                        "(vertex INTEGER PRIMARY KEY, x DOUBLE, y DOUBLE, name TEXT)"
                statement.execute(query)
            }

            val insertQuery = "INSERT INTO vertices(vertex, x, y, name) VALUES(?, ?, ?, ?)"
            conn.prepareStatement(insertQuery).use { prepStatement ->
                var index = -1
                val indexation: Map<Vertex, Int> =
                    this.graph.vertices.associate { vertex ->
                        index += 1
                        vertex to index
                    }
                indexation.keys.forEach { vertex ->
                    prepStatement.setInt(1, indexation[vertex]!!)
                    prepStatement.setDouble(2, vertex.x)
                    prepStatement.setDouble(3, vertex.y)
                    prepStatement.setString(4, vertex.name)
                    println(vertex.name)
                    prepStatement.addBatch()
                }
                prepStatement.executeBatch()
                return indexation
            }
        } catch (e: Exception) {
            println(e)
            return null
        }
    }

    private fun loadEdges(
        conn: Connection,
        indexation: Map<Vertex, Int>,
    ): Boolean {
        try {
            conn.createStatement().use { statement ->
                val query =
                    "CREATE TABLE edges " +
                        "(from_vertex INTEGER, to_vertex INTEGER, weight DOUBLE)"
                statement.execute(query)
            }

            val insertQuery = "INSERT INTO edges(from_vertex, to_vertex, weight) VALUES(?, ?, ?)"
            conn.prepareStatement(insertQuery).use { prepStatement ->
                this.graph.edges.keys.forEach { vertex ->
                    graph.edges[vertex]!!.forEach {
                        prepStatement.setInt(1, indexation[it.from]!!)
                        prepStatement.setInt(2, indexation[it.to]!!)
                        prepStatement.setDouble(3, it.weight)
                        prepStatement.addBatch()
                    }
                }
                prepStatement.executeBatch()
            }
            return true
        } catch (e: Exception) {
            println(e)
            return false
        }
    }
}
