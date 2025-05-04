package model_tests

import graph.*
import model.algo.build_mst
import org.junit.jupiter.api.*
import kotlin.test.assertEquals

class mst_test {
    private lateinit var g: Graph

    @BeforeEach
    fun setup() {
        g = Graph(false, true)
    }

    @Test
    @DisplayName("simplest mst possible")
    fun simple_mst() {
        val a = Vertex(1.0, 1.0)
        val b = Vertex(1.0, 1.0)
        val c = Vertex(1.0, 1.0)

        g.add_vertex(a)
        g.add_vertex(b)
        g.add_vertex(c)

        g.add_edge(a, b, 1.0)
        g.add_edge(a, c, 1.0)

        val exp_mst = Graph(false, true)
        exp_mst.add_vertex(a)
        exp_mst.add_vertex(b)
        exp_mst.add_vertex(c)
        exp_mst.add_edge(a, b, 1.0)
        exp_mst.add_edge(a, c, 1.0)

        val actual_mst = g.build_mst()
        assertEquals(false, actual_mst == null)
        assertEquals(exp_mst.vertices, actual_mst!!.vertices)
        assertEquals(exp_mst.edges, actual_mst!!.edges)
    }

    @Test
    @DisplayName("Simple graph where deletion of some edges is needed")
    fun extra_edges_in_simple_graph() {
        val a = Vertex(1.0, 1.0)
        val b = Vertex(1.0, 1.0)
        val c = Vertex(1.0, 1.0)

        g.add_vertex(a)
        g.add_vertex(b)
        g.add_vertex(c)

        g.add_edge(a, b, 1.0)
        g.add_edge(a, c, 10000.0)
        g.add_edge(b, c, 1.0)

        val exp_mst = Graph(false, true)
        exp_mst.add_vertex(a)
        exp_mst.add_vertex(b)
        exp_mst.add_vertex(c)
        exp_mst.add_edge(a, b, 1.0)
        exp_mst.add_edge(b, c, 1.0)

        val actual_mst = g.build_mst()
        val actual_weight = actual_mst!!.edges.keys.sumOf { vertex -> actual_mst.edges[vertex]!!
                                                                   .sumOf { it.weight } }
        assertEquals(2.0, actual_weight / 2)
        assertEquals(exp_mst.vertices, actual_mst.vertices)
        assertEquals(exp_mst.edges, actual_mst.edges)
    }

    @Test
    @DisplayName("Another test on simple graph with extra edges")
    fun simple_graph_4_vertices() {
        val my_vertices = mutableListOf<Vertex>()
        repeat(4) {
            my_vertices.add(Vertex(1.0, 1.0))
        }

        repeat(4) {
            g.add_vertex(my_vertices[it])
        }

        g.add_edge(my_vertices[0], my_vertices[1], 1.0)
        g.add_edge(my_vertices[0], my_vertices[2], 1.0)
        g.add_edge(my_vertices[0], my_vertices[3], 1.0)

        g.add_edge(my_vertices[1], my_vertices[2], 10.0)
        g.add_edge(my_vertices[2], my_vertices[3], 10.0)

        val exp_mst = Graph(false, true)
        repeat(4) {
            exp_mst.add_vertex(my_vertices[it])
        }

        exp_mst.add_edge(my_vertices[0], my_vertices[1], 1.0)
        exp_mst.add_edge(my_vertices[0], my_vertices[2], 1.0)
        exp_mst.add_edge(my_vertices[0], my_vertices[3], 1.0)

        val actual_mst = g.build_mst()
        val actual_weight = actual_mst!!.edges.keys.sumOf { vertex -> actual_mst.edges[vertex]!!
            .sumOf { it.weight } }
        assertEquals(3.0, actual_weight / 2)
        assertEquals(exp_mst.vertices, actual_mst.vertices)
        assertEquals(exp_mst.edges, actual_mst.edges)
    }

}
