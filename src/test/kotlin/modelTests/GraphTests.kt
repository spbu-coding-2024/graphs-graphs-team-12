package modelTests

import graph.Graph
import graph.Vertex
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class GraphTests {
    @Test
    @DisplayName("Edge with same start and end vertices")
    fun edge_same_vertices() {
        val g = Graph(false, false)
        val x = Vertex(1.0, 1.0)
        g.addVertex(x)
        assertFalse(g.addEdge(x, x))
    }

    @Test
    @DisplayName("Edge with weight in not weighted graph")
    fun weight_in_not_weighted() {
        val g = Graph(false, false)
        val x = Vertex(1.0, 1.0)
        val y = Vertex(1.0, 1.0)
        g.addVertex(x)
        g.addVertex(y)
        assertFalse(g.addEdge(x, y, 2.0))
    }

    @Test
    @DisplayName("Edge that already exists")
    fun add_same_edge() {
        val g = Graph(false, false)
        val x = Vertex(1.0, 1.0)
        val y = Vertex(1.0, 1.0)
        g.addVertex(x)
        g.addVertex(y)
        g.addEdge(x, y)
        assertFalse(g.addEdge(x, y))
    }

    @Test
    @DisplayName("Adjacency list for empty graph")
    fun empty_adj_list() {
        val g = Graph(false, false)
        assertEquals(emptyMap<Vertex, Set<Pair<Vertex, Double>>>(), g.getAdjacencyList())
    }

    @Test
    @DisplayName("Adjacency list of simple graph")
    fun simple_adj_list() {
        val g = Graph(false, false)
        val x = Vertex(1.0, 1.0)
        val y = Vertex(1.0, 1.0)
        val z = Vertex(1.0, 1.0)
        g.addVertex(x)
        g.addVertex(y)
        g.addVertex(z)
        g.addEdge(x, y)
        assertEquals(mapOf<Vertex, Set<Pair<Vertex, Double>>>(x to setOf(y to 1.0), y to setOf(x to 1.0)), g.getAdjacencyList())
    }

    @Test
    @DisplayName("Adjacency list of some else graph")
    fun bigger_graph_adj_list() {
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
        g.addEdge(a, b)
        g.addEdge(b, x)
        g.addEdge(x, y)
        g.addEdge(b, y)
        assertEquals(
            mapOf<Vertex, Set<Pair<Vertex, Double>>>(
                a to setOf(b to 1.0),
                b to setOf(a to 1.0, x to 1.0, y to 1.0),
                x to setOf(b to 1.0, y to 1.0),
                y to setOf(b to 1.0, x to 1.0),
            ),
            g.getAdjacencyList(),
        )
    }

    @Test
    @DisplayName("Adjacency list of simple directed and weighted graph")
    fun adj_list_of_directed_and_weighted() {
        val g = Graph(true, true)
        val x = Vertex(1.0, 1.0)
        val y = Vertex(1.0, 1.0)
        val z = Vertex(1.0, 1.0)
        g.addVertex(x)
        g.addVertex(y)
        g.addVertex(z)
        g.addEdge(x, y, 10.0)
        g.addEdge(x, z, 5.0)
        assertEquals(mapOf<Vertex, Set<Pair<Vertex, Double>>>(x to setOf(y to 10.0, z to 5.0)), g.getAdjacencyList())
    }

    @Test
    @DisplayName("Another test of directed weighted graph adjacency list")
    fun bigger_adj_list_directed_weighted() {
        val g = Graph(true, true)
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
        g.addEdge(a, b, 3.0)
        g.addEdge(a, y, 15.0)
        g.addEdge(b, x, 5.0)
        g.addEdge(x, y, 10.0)
        g.addEdge(y, x, 2.0)
        g.addEdge(y, b, 8.0)
        assertEquals(
            mapOf<Vertex, Set<Pair<Vertex, Double>>>(
                a to setOf(b to 3.0, y to 15.0),
                b to setOf(x to 5.0),
                x to setOf(y to 10.0),
                y to setOf(b to 8.0, x to 2.0),
            ),
            g.getAdjacencyList(),
        )
    }

    @Test
    @DisplayName("Adjacency matrix for empty graph")
    fun empty_adj_matrix() {
        val g = Graph(false, false)
        assertEquals(emptyMap<Vertex, MutableMap<Vertex, Double?>>(), g.getAdjacencyMatrix())
    }

    @Test
    @DisplayName("Adjacency matrix of simple graph")
    fun simple_adj_matrix() {
        val g = Graph(false, false)
        val x = Vertex(1.0, 1.0)
        val y = Vertex(1.0, 1.0)
        val z = Vertex(1.0, 1.0)
        g.addVertex(x)
        g.addVertex(y)
        g.addVertex(z)
        g.addEdge(x, y)
        assertEquals(
            mapOf<Vertex, MutableMap<Vertex, Double?>>(
                x to mutableMapOf(x to null, y to 1.0, z to null),
                y to mutableMapOf(x to 1.0, y to null, z to null),
                z to mutableMapOf(x to null, y to null, z to null),
            ),
            g.getAdjacencyMatrix(),
        )
    }

    @Test
    @DisplayName("Adjacency matrix of some else graph")
    fun bigger_graph_adj_matrix() {
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
        g.addEdge(a, b)
        g.addEdge(b, x)
        g.addEdge(x, y)
        g.addEdge(b, y)
        assertEquals(
            mapOf<Vertex, MutableMap<Vertex, Double?>>(
                a to mutableMapOf(a to null, b to 1.0, x to null, y to null, z to null),
                b to mutableMapOf(a to 1.0, b to null, x to 1.0, y to 1.0, z to null),
                x to mutableMapOf(a to null, b to 1.0, x to null, y to 1.0, z to null),
                y to mutableMapOf(a to null, b to 1.0, x to 1.0, y to null, z to null),
                z to mutableMapOf(a to null, b to null, x to null, y to null, z to null),
            ),
            g.getAdjacencyMatrix(),
        )
    }

    @Test
    @DisplayName("Adjacency matrix of simple directed and weighted graph")
    fun adj_matrix_of_directed_and_weighted() {
        val g = Graph(true, true)
        val x = Vertex(1.0, 1.0)
        val y = Vertex(1.0, 1.0)
        val z = Vertex(1.0, 1.0)
        g.addVertex(x)
        g.addVertex(y)
        g.addVertex(z)
        g.addEdge(x, y, 10.0)
        g.addEdge(x, z, 5.0)
        assertEquals(
            mapOf<Vertex, MutableMap<Vertex, Double?>>(
                x to mutableMapOf(x to null, y to 10.0, z to 5.0),
                y to mutableMapOf(x to null, y to null, z to null),
                z to mutableMapOf(x to null, y to null, z to null),
            ),
            g.getAdjacencyMatrix(),
        )
    }

    @Test
    @DisplayName("Another test of directed weighted graph adjacency matrix")
    fun bigger_adj_matrix_directed_weighted() {
        val g = Graph(true, true)
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
        g.addEdge(a, b, 3.0)
        g.addEdge(a, y, 15.0)
        g.addEdge(b, x, 5.0)
        g.addEdge(x, y, 10.0)
        g.addEdge(y, x, 2.0)
        g.addEdge(y, b, 8.0)
        assertEquals(
            mapOf<Vertex, MutableMap<Vertex, Double?>>(
                a to mutableMapOf(a to null, b to 3.0, x to null, y to 15.0, z to null),
                b to mutableMapOf(a to null, b to null, x to 5.0, y to null, z to null),
                x to mutableMapOf(a to null, b to null, x to null, y to 10.0, z to null),
                y to mutableMapOf(a to null, b to 8.0, x to 2.0, y to null, z to null),
                z to mutableMapOf(a to null, b to null, x to null, y to null, z to null),
            ),
            g.getAdjacencyMatrix(),
        )
    }
}
