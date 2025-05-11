package model_tests

import graph.*
import org.junit.jupiter.api.*
import kotlin.test.assertEquals

class Graph {
    @Test
    @DisplayName("Adjacency list for empty graph")
    fun empty_adj_list() {
        val g = Graph(false, false, false)
        assertEquals(emptyMap(), g.get_adjacency_list())
    }

    @Test
    @DisplayName("Adjacency list of simple graph")
    fun simple_adj_list() {
        val g = Graph(false, false, false)
        val x = Vertex(1.0, 1.0)
        val y = Vertex(1.0, 1.0)
        val z = Vertex(1.0, 1.0)
        g.add_vertex(x)
        g.add_vertex(y)
        g.add_vertex(z)
        g.add_edge(x, y)
        assertEquals(mapOf<Vertex, Set<Pair<Vertex, Double>>>(x to setOf(y to 1.0), y to setOf(x to 1.0)), g.get_adjacency_list())
    }

    @Test
    @DisplayName("Adjacency list of some else graph")
    fun bigger_graph_adj_list() {
    }

    @Test
    @DisplayName("Adjacency list of simple directed and weighted graph")
    fun adj_list_of_directed_and_weighted() {
    }

    @Test
    @DisplayName("Another test of directed weighted graph adjacency list")
    fun adj_list_directed_weighted() {
    }

    fun more_tests_to_do() {
        TODO("check adjacency matrix as well")
    }
}
