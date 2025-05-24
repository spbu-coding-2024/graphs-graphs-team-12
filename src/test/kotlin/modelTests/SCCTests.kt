package modelTests

import graph.Graph
import graph.Vertex
import model.algo.findSCC
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SCCTests {
    lateinit var g: Graph

    @BeforeEach
    fun setup() {
        g = Graph(true, false)
    }

    @DisplayName("One component of two vertices")
    @Test
    fun simpleOneComponent() {
        val firstVertex = Vertex(1.0, 1.0)
        val secondVertex = Vertex(1.0, 1.0)

        g.addVertex(firstVertex)
        g.addVertex(secondVertex)

        g.addEdge(firstVertex, secondVertex)
        g.addEdge(secondVertex, firstVertex)

        val expected =
            mutableSetOf<MutableSet<Vertex>>(
                mutableSetOf(firstVertex, secondVertex),
            )
        assertEquals(expected, g.findSCC())
    }

    @DisplayName("Two isolated vertices")
    @Test
    fun isolated() {
        val firstVertex = Vertex(1.0, 1.0)
        val secondVertex = Vertex(1.0, 1.0)

        g.addVertex(firstVertex)
        g.addVertex(secondVertex)

        val expected =
            mutableSetOf<MutableSet<Vertex>>(
                mutableSetOf(firstVertex),
                mutableSetOf(secondVertex),
            )
        assertEquals(expected, g.findSCC())
    }

    @DisplayName("Two distinct communities")
    @Test
    fun twoDistinctComponents() {
        val vertices = arrayListOf<Vertex>()
        repeat(4) {
            vertices.add(Vertex(1.0, 1.0))
            g.addVertex(vertices[it])
        }

        g.addEdge(vertices[0], vertices[1])
        g.addEdge(vertices[1], vertices[0])
        g.addEdge(vertices[1], vertices[2])
        g.addEdge(vertices[2], vertices[1])

        val expected =
            mutableSetOf<MutableSet<Vertex>>(
                mutableSetOf(vertices[0], vertices[1], vertices[2]),
                mutableSetOf(vertices[3]),
            )
        assertEquals(expected, g.findSCC())
    }

    @DisplayName("Run on undirected graph")
    @Test
    fun unsuitableGraph() {
        val undirGraph = Graph(false, false)
        val onlyVertex = Vertex(1.0, 1.0)
        undirGraph.addVertex(onlyVertex)
        assertEquals(null, undirGraph.findSCC())
    }
}
