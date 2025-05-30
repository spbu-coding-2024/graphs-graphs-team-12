package modelTests.algo

import graph.Graph
import graph.Vertex
import model.algo.getHarmonicCentrality
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HarmonicCentralityTest {
    lateinit var g: Graph

    @BeforeEach
    fun setup() {
        g = Graph(true, true)
    }

    @Test
    fun `harmonic centrality in graph with negative edges`() {
        val x = Vertex(0.0, 0.0)
        val y = Vertex(0.0, 0.0)
        g.addVertex(x)
        g.addVertex(y)
        g.addEdge(x, y, -10.0)
        assertNull(getHarmonicCentrality(g, x))
    }

    @Test
    fun `harmonic centrality of isolated vertex`() {
        val x = Vertex(0.0, 0.0)
        g.addVertex(x)
        assertEquals(0.0, getHarmonicCentrality(g, x))
    }

    @Test
    fun `harmonic centrality of vertex in simple graph`() {
        val x = Vertex(0.0, 0.0)
        val y = Vertex(0.0, 0.0)
        val z = Vertex(0.0, 0.0)
        g.addVertex(x)
        g.addVertex(y)
        g.addVertex(z)
        g.addEdge(x, y, 10.0)
        g.addEdge(y, z, 5.0)
        assertEquals(0.2, getHarmonicCentrality(g, y) ?: -1.0, 1e-6)
    }

    @Test
    fun `harmonic centrality of vertex in  bigger graph`() {
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
        assertEquals(314.0 / 195.0, getHarmonicCentrality(g, a) ?: -1.0, 1e-6)
    }
}
