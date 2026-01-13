package modelTests.algo

import graph.Graph
import graph.Vertex
import model.algo.findBridges
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FindBridges {
    lateinit var g: Graph

    @BeforeEach
    fun setup() {
        g = Graph(directed = false, weighted = false)
    }

    @Test
    fun `dumbbell graph (classic bridge)`() {
        val a = Vertex(0.0, 0.0, "A")
        val b = Vertex(0.0, 0.0, "B")
        val c = Vertex(0.0, 0.0, "C")
        val e = Vertex(0.0, 0.0, "E")
        val f = Vertex(0.0, 0.0, "F")
        val z = Vertex(0.0, 0.0, "Z")
        g.addVertex(a)
        g.addVertex(b)
        g.addVertex(c)
        g.addVertex(e)
        g.addVertex(f)
        g.addVertex(z)
        g.addEdge(a, b)
        g.addEdge(b, c)
        g.addEdge(c, a)
        g.addEdge(e, f)
        g.addEdge(f, z)
        g.addEdge(z, e)
        g.addEdge(c, e)
        val bridges = findBridges(g)
        assertEquals(1, bridges.size)
        val bridge = bridges.first()
        val isCorrectBridge = (bridge.from == c && bridge.to == e) || (bridge.from == e && bridge.to == c)
        assertTrue(isCorrectBridge)
    }

    @Test
    fun `simple cycle (no bridges)`() {
        val a = Vertex(0.0, 0.0, "A")
        val b = Vertex(0.0, 0.0, "B")
        val c = Vertex(0.0, 0.0, "C")

        g.addVertex(a)
        g.addVertex(b)
        g.addVertex(c)
        g.addEdge(a, b)
        g.addEdge(b, c)
        g.addEdge(c, a)
        val bridges = findBridges(g)
        assertEquals(0, bridges.size)
    }

    @Test
    fun `tree graph (all edges are bridges)`() {
        val a = Vertex(0.0, 0.0, "A")
        val b = Vertex(0.0, 0.0, "B")
        val c = Vertex(0.0, 0.0, "C")

        g.addVertex(a)
        g.addVertex(b)
        g.addVertex(c)
        g.addEdge(a, b)
        g.addEdge(b, c)
        val bridges = findBridges(g)
        assertEquals(2, bridges.size)
    }

    @Test
    fun `disconnected graph`() {
        val a = Vertex(0.0, 0.0, "A")
        val b = Vertex(0.0, 0.0, "B")
        val c = Vertex(0.0, 0.0, "C")
        val d = Vertex(0.0, 0.0, "D")
        g.addVertex(a)
        g.addVertex(b)
        g.addVertex(c)
        g.addVertex(d)
        g.addEdge(a, b)
        g.addEdge(c, d)
        val bridges = findBridges(g)
        assertEquals(2, bridges.size)
    }
}
