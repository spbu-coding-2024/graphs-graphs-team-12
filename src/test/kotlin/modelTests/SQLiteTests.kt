package modelTests

import graph.Graph
import graph.Vertex
import model.io.sqliteIO.GraphLoader
import model.io.sqliteIO.GraphReader
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.sql.DriverManager
import kotlin.test.assertEquals

class SQLiteTests {
    private lateinit var g: Graph
    private val path = "src/test/kotlin/modelTests/forDbFiles/graph.db"

    @AfterEach
    fun cleanUp() {
        val conn = DriverManager.getConnection("jdbc:sqlite:$path")
        conn.createStatement().execute("DROP TABLE IF EXISTS properties")
        conn.createStatement().execute("DROP TABLE IF EXISTS vertices")
        conn.createStatement().execute("DROP TABLE IF EXISTS edges")
        conn.close()
    }

    @DisplayName("Simple graph without edges")
    @Test
    fun isoVertices() {
        g = Graph(false, false)
        val firstVertex = Vertex(1.0, 10.0)
        val secondVertex = Vertex(100.0, 1.0)
        g.addVertex(firstVertex)
        g.addVertex(secondVertex)

        val loader = GraphLoader(g, path)
        assertEquals(true, loader.loadGraph())

        val reader = GraphReader(path)
        val loadedG = reader.readGraph()

        assertEquals(true, loadedG != null)
        assertEquals(loadedG!!.directed, g.directed)
        assertEquals(loadedG.weighted, g.weighted)
        assertEquals(true, loadedG.vertices.size == g.vertices.size)
        assertEquals(true, loadedG.edges == g.edges)
        assertEquals(true, loadedG.vertices.any { it.x == 100.00 })
        assertEquals(true, loadedG.vertices.any { it.y == 10.0 })
    }

    @DisplayName("Simple graph with some edges as well")
    @Test
    fun simpleGraph() {
        g = Graph(true, true)
        val vertices = arrayListOf<Vertex>()
        repeat(3) {
            vertices.add(Vertex(1.0, 1.0, "vertex $it"))
            g.addVertex(vertices[it])
        }
        g.addEdge(vertices[0], vertices[1], 10.0)
        g.addEdge(vertices[0], vertices[2], 20.0)
        g.addEdge(vertices[2], vertices[0], 5.0)

        val loader = GraphLoader(g, path)
        assertEquals(true, loader.loadGraph())

        val reader = GraphReader(path)
        val loadedG = reader.readGraph()
        assertEquals(true, loadedG != null)
        assertEquals(g.vertices.size, loadedG!!.vertices.size)

        val loadedEdges = loadedG.edges.flatMap { it.value }
        assertEquals(3, loadedEdges.size)
        assertEquals(true, loadedEdges.any { it.weight == 20.0 })
        val firstEdge = loadedEdges.first { it.weight == 20.0 }
        assertEquals(true, loadedEdges.any { it.weight == 5.0 })
        val secondEdge = loadedEdges.first { it.weight == 5.0 }
        assertEquals(firstEdge.from, secondEdge.to)
        assertEquals(firstEdge.to, secondEdge.from)
    }
}
