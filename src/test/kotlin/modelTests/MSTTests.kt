package modelTests

import graph.Edge
import graph.Graph
import graph.Vertex
import model.algo.buildMST
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MSTTests {
    lateinit var g: Graph

    fun weightOfTree(tree: ArrayList<Edge>): Double {
        val ans = tree.fold(0.0) { value, edge -> value + edge.weight }
        return ans
    }

    @BeforeEach
    fun setup() {
        g = Graph(false, true)
    }

    @DisplayName("MST of directed graph")
    @Test
    fun unsuitableGraph() {
        val badG = Graph(true, true)
        assertEquals(null, badG.buildMST())
    }

    @DisplayName("MST of unweighted graph")
    @Test
    fun anotherUnsuitableGraph() {
        val badG = Graph(false, false)
        assertEquals(null, badG.buildMST())
    }

    @DisplayName("Disconnected graph")
    @Test
    fun disconnectedGraph() {
        val firstVertex = Vertex(1.0, 1.0)
        val secondVertex = Vertex(1.0, 1.0)
        g.addVertex(firstVertex)
        g.addVertex(secondVertex)
        assertEquals(null, g.buildMST())
    }

    @DisplayName("Simple graph, no extra edges")
    @Test
    fun simplestGraph() {
        val firstVertex = Vertex(1.0, 1.0)
        val secondVertex = Vertex(1.0, 1.0)
        g.addVertex(firstVertex)
        g.addVertex(secondVertex)
        g.addEdge(firstVertex, secondVertex)
        val expected = 1.0

        assertEquals(expected, weightOfTree(g.buildMST() ?: arrayListOf()))
    }

    @DisplayName("Triangle")
    @Test
    fun triangleGraph() {
        val vertices = ArrayList<Vertex>()
        repeat(3) {
            vertices.add(Vertex(1.0, 1.0))
            g.addVertex(vertices[it])
        }
        g.addEdge(vertices[0], vertices[1])
        g.addEdge(vertices[1], vertices[2])
        g.addEdge(vertices[0], vertices[2], 100.0)

        val expected = 2.0
        assertEquals(expected, weightOfTree(g.buildMST() ?: arrayListOf()))
    }
}
