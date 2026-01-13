package modelTests.algo

import graph.Graph
import graph.Vertex
import model.algo.bellman
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BellmanTest {
    lateinit var g: Graph

    @BeforeEach
    fun setup() {
        g = Graph(true, true)
    }

    @Test
    fun `bellman-ford on simple graph with positive weights`() {
        val start = Vertex(0.0, 0.0, "Start")
        val mid = Vertex(0.0, 0.0, "Mid")
        val end = Vertex(0.0, 0.0, "End")

        g.addVertex(start)
        g.addVertex(mid)
        g.addVertex(end)

        g.addEdge(start, mid, 10.0)
        g.addEdge(mid, end, 5.0)
        g.addEdge(start, end, 20.0)

        val result = bellman(g, start, end)

        assertEquals(15.0, result?.first)
        assertEquals(listOf(start, mid, end), result?.second)
    }

    @Test
    fun `bellman-ford with negative edge (no cycle)`() {
        val a = Vertex(0.0, 0.0, "A")
        val b = Vertex(0.0, 0.0, "B")
        val c = Vertex(0.0, 0.0, "C")
        g.addVertex(a)
        g.addVertex(b)
        g.addVertex(c)
        g.addEdge(a, b, 5.0)
        g.addEdge(b, c, -2.0)
        val result = bellman(g, a, c)
        assertEquals(3.0, result?.first)
        assertEquals(listOf(a, b, c), result?.second)
    }

    @Test
    fun `bellman-ford detects negative cycle`() {
        val a = Vertex(0.0, 0.0, "A")
        val b = Vertex(0.0, 0.0, "B")
        val c = Vertex(0.0, 0.0, "C")

        g.addVertex(a)
        g.addVertex(b)
        g.addVertex(c)

        g.addEdge(a, b, 1.0)
        g.addEdge(b, c, -5.0)
        g.addEdge(c, a, 1.0)
        val result = bellman(g, a, b)
        assertNull(result)
    }

    @Test
    fun `bellman-ford no path`() {
        val start = Vertex(0.0, 0.0, "Start")
        val end = Vertex(0.0, 0.0, "End")
        g.addVertex(start)
        g.addVertex(end)
        val result = bellman(g, start, end)

        assertNull(result)
    }

    @Test
    fun `bellman-ford on undirected graph`() {
        val undirectedG = Graph(directed = false, weighted = true)
        val a = Vertex(0.0, 0.0, "A")
        val b = Vertex(0.0, 0.0, "B")

        undirectedG.addVertex(a)
        undirectedG.addVertex(b)
        undirectedG.addEdge(a, b, 10.0)

        val res1 = bellman(undirectedG, a, b)
        assertEquals(10.0, res1?.first)

        val res2 = bellman(undirectedG, b, a)
        assertEquals(10.0, res2?.first)
    }
}
