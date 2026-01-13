package modelTests

import graph.Graph
import graph.Vertex
import model.io.json.JsonGraphIO
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class JsonIOTest {
    private val path = "json_test_graph.json"
    private val io = JsonGraphIO()

    @AfterEach
    fun cleanUp() {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
    }

    @Test
    fun `save and load graph with isolated vertices`() {
        val originalGraph = Graph(directed = false, weighted = false)
        val v1 = Vertex(10.0, 20.0, "A")
        val v2 = Vertex(30.0, 40.0, "B")
        originalGraph.addVertex(v1)
        originalGraph.addVertex(v2)
        io.saveGraph(originalGraph, path)
        val loadedGraph = io.loadGraph(path)
        assertEquals(originalGraph.directed, loadedGraph.directed)
        assertEquals(originalGraph.weighted, loadedGraph.weighted)
        assertEquals(2, loadedGraph.vertices.size)
        val loadedV1 = loadedGraph.vertices.find { it.name == "A" }
        assertNotNull(loadedV1)
        assertEquals(10.0, loadedV1.x)
        assertEquals(20.0, loadedV1.y)
    }

    @Test
    fun `save and load directed weighted graph with edges`() {
        val originalGraph = Graph(directed = true, weighted = true)
        val v1 = Vertex(0.0, 0.0, "Start")
        val v2 = Vertex(10.0, 10.0, "End")
        originalGraph.addVertex(v1)
        originalGraph.addVertex(v2)
        originalGraph.addEdge(v1, v2, 5.5)
        io.saveGraph(originalGraph, path)
        val loadedGraph = io.loadGraph(path)

        assertTrue(loadedGraph.directed)
        assertTrue(loadedGraph.weighted)
        val allEdges = loadedGraph.edges.values.flatten()
        assertEquals(1, allEdges.size)
        val edge = allEdges.first()
        assertEquals("Start", edge.from.name)
        assertEquals("End", edge.to.name)
        assertEquals(5.5, edge.weight)
    }

    @Test
    fun `load non-existent file throws exception`() {
        val badPath = "non_existent_file.json"
        try {
            io.loadGraph(badPath)
            assertTrue(false)
        } catch (e: Exception) {
            assertEquals("Файл не найден", e.message)
        }
    }

    @Test
    fun `overwrite existing file`() {
        val g1 = Graph(false, false)
        g1.addVertex(Vertex(0.0, 0.0, "V1"))
        io.saveGraph(g1, path)
        val g2 = Graph(false, false)
        io.saveGraph(g2, path)
        val loaded = io.loadGraph(path)
        assertEquals(0, loaded.vertices.size)
    }
}
