package modelTests.algo

import model.algo.findCycles
import graph.Graph
import graph.Vertex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class FindCyclesTest {
    @Test
    @DisplayName("Find cycles in graph with only one vertex")
    fun `Graph with one vertex`() {
        val g = Graph(false, false)
        val x = Vertex(1.0, 1.0)
        g.addVertex(x)
        assertEquals(emptySet<List<Vertex>>(), findCycles(g, x))
    }

    @Test
    @DisplayName("Find cycles in undirected graph without cycles")
    fun `Undirected graph without cycles`() {
        val g = Graph(false, false)
        val x = Vertex(1.0, 1.0)
        val y = Vertex(1.0, 1.0)
        val z = Vertex(1.0, 1.0)
        g.addVertex(x)
        g.addVertex(y)
        g.addVertex(z)
        g.addEdge(x, y)
        g.addEdge(y, z)
        assertEquals(emptySet<List<Vertex>>(), findCycles(g, x))
    }

    @Test
    @DisplayName("Find cycles in undirected graph with 3 element cycle")
    fun `Undirected graph with one cycle`() {
        val g = Graph(false, false)
        val x = Vertex(1.0, 1.0)
        val y = Vertex(1.0, 1.0)
        val z = Vertex(1.0, 1.0)
        g.addVertex(x)
        g.addVertex(y)
        g.addVertex(z)
        g.addEdge(x, y)
        g.addEdge(x, z)
        g.addEdge(y, z)
        assertEquals(setOf<List<Vertex>>(listOf(x, y, z), listOf(x, z, y)), findCycles(g, x))
    }

    @Test
    @DisplayName("Find cycles in undirected graph with two cycles")
    fun `Undirected graph with two cycles`() {
        val g = Graph(false, false)
        val a = Vertex(1.0, 1.0)
        val b = Vertex(1.0, 1.0)
        val x = Vertex(1.0, 1.0)
        val y = Vertex(1.0, 1.0)
        val z = Vertex(1.0, 1.0)
        g.addVertex(a)
        g.addVertex(b)
        g.addVertex(x)
        g.addVertex(y)
        g.addVertex(z)
        g.addEdge(x, a)
        g.addEdge(x, b)
        g.addEdge(a, b)
        g.addEdge(x, y)
        g.addEdge(x, z)
        g.addEdge(y, z)
        assertEquals(
            setOf<List<Vertex>>(
                listOf(x, y, z),
                listOf(x, z, y),
                listOf(x, a, b),
                listOf(x, b, a),
            ),
            findCycles(g, x),
        )
    }

    @Test
    @DisplayName("Find cycles in directed graph without cycles")
    fun `Directed graph without cycles`() {
        val g = Graph(true, false)
        val x = Vertex(1.0, 1.0)
        val y = Vertex(1.0, 1.0)
        val z = Vertex(1.0, 1.0)
        g.addVertex(x)
        g.addVertex(y)
        g.addVertex(z)
        g.addEdge(x, y)
        g.addEdge(x, z)
        g.addEdge(y, z)
        assertEquals(emptySet<List<Vertex>>(), findCycles(g, x))
    }

    @Test
    @DisplayName("Find cycles in directed graph with 3 element cycle")
    fun `Directed graph with one cycle`() {
        val g = Graph(true, false)
        val x = Vertex(1.0, 1.0)
        val y = Vertex(1.0, 1.0)
        val z = Vertex(1.0, 1.0)
        g.addVertex(x)
        g.addVertex(y)
        g.addVertex(z)
        g.addEdge(x, y)
        g.addEdge(x, z)
        g.addEdge(y, z)
        g.addEdge(z, x)
        assertEquals(setOf<List<Vertex>>(listOf(x, y, z), listOf(x, z)), findCycles(g, x))
    }

    @Test
    @DisplayName("Find cycles in directed graph with two cycles")
    fun `Directed graph with two cycles`() {
        val g = Graph(true, false)
        val a = Vertex(1.0, 1.0)
        val b = Vertex(1.0, 1.0)
        val x = Vertex(1.0, 1.0)
        val y = Vertex(1.0, 1.0)
        val z = Vertex(1.0, 1.0)
        g.addVertex(a)
        g.addVertex(b)
        g.addVertex(x)
        g.addVertex(y)
        g.addVertex(z)
        g.addEdge(x, a)
        g.addEdge(a, b)
        g.addEdge(b, x)
        g.addEdge(x, y)
        g.addEdge(y, z)
        g.addEdge(z, x)
        assertEquals(setOf<List<Vertex>>(listOf(x, y, z), listOf(x, a, b)), findCycles(g, x))
    }
}
