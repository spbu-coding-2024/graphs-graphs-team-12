package modelTests.algo

import graph.Graph
import graph.Vertex
import model.algo.dijkstra
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DijkstraTest {
    lateinit var g: Graph

    @BeforeEach
    fun setup() {
        g = Graph(true, true)
    }

    @Test
    fun `dijkstra on graph with negative-weight edge`() {
        val x = Vertex(0.0, 0.0)
        val y = Vertex(0.0, 0.0)
        g.addVertex(x)
        g.addVertex(y)
        g.addEdge(x, y, -10.0)
        assertNull(dijkstra(g, x, y))
    }

    @Test
    fun `dijkstra on graph without path`() {
        val x = Vertex(0.0, 0.0)
        val y = Vertex(0.0, 0.0)
        val z = Vertex(0.0, 0.0)
        g.addVertex(x)
        g.addVertex(y)
        g.addVertex(z)
        g.addEdge(x, y, 5.0)
        assertEquals(Pair(Double.MAX_VALUE, emptyList()), dijkstra(g, x, z))
    }

    @Test
    fun `dijkstra on simple graph`() {
        val x = Vertex(0.0, 0.0)
        val y = Vertex(0.0, 0.0)
        val z = Vertex(0.0, 0.0)
        g.addVertex(x)
        g.addVertex(y)
        g.addVertex(z)
        g.addEdge(x, y, 10.0)
        g.addEdge(y, z, 5.0)
        g.addEdge(x, z, 20.0)
        assertEquals(Pair(15.0, listOf(x, y, z)), dijkstra(g, x, z))
    }

    @Test
    fun `dijkstra on bigger graph`() {
        val a = Vertex(0.0, 0.0)
        val b = Vertex(0.0, 0.0)
        val x = Vertex(0.0, 0.0)
        val y = Vertex(0.0, 0.0)
        val z = Vertex(0.0, 0.0)
        g.addVertex(a)
        g.addVertex(b)
        g.addVertex(x)
        g.addVertex(y)
        g.addVertex(z)
        g.addEdge(a, x, 5.0)
        g.addEdge(x, b, 2.0)
        g.addEdge(b, z, 20.0)
        g.addEdge(x, z, 8.0)
        g.addEdge(a, b, 3.0)
        g.addEdge(a, y, 1.0)
        g.addEdge(y, z, 30.0)
        g.addEdge(a, z, 50.0)
        assertEquals(Pair(13.0, listOf(a, x, z)), dijkstra(g, a, z))
    }
}
