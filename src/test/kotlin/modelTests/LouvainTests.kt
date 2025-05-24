package modelTests

import graph.Graph
import graph.Vertex
import model.algo.louvain
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LouvainTests {
    lateinit var g: Graph

    @DisplayName("Two isolated vertices")
    @Test
    fun isoedVertices() {
        g = Graph(false, false)
        val firstVertex = Vertex(1.0, 1.0)
        val secondVertex = Vertex(1.0, 1.0)
        g.addVertex(firstVertex)
        g.addVertex(secondVertex)
        val communities = g.louvain()
        assertEquals(true, communities[firstVertex] != -1)
        assertEquals(true, communities[secondVertex] != -1)
        assertEquals(true, communities[firstVertex] != communities[secondVertex])
    }

    @DisplayName("Two connected vertices")
    @Test
    fun connectedVertices() {
        g = Graph(true, false)
        val firstVertex = Vertex(1.0, 1.0)
        val secondVertex = Vertex(1.0, 1.0)
        g.addVertex(firstVertex)
        g.addVertex(secondVertex)
        g.addEdge(firstVertex, secondVertex)
        g.addEdge(secondVertex, firstVertex)
        val communities = g.louvain()
        assertEquals(true, communities[firstVertex] == communities[secondVertex])
    }

    @DisplayName("Two communities")
    @Test
    fun twoCommunities() {
        g = Graph(false, true)
        val vertices =
            listOf<Vertex>(
                Vertex(1.0, 1.0),
                Vertex(1.0, 1.0),
                Vertex(1.0, 1.0),
                Vertex(1.0, 1.0),
            )
        vertices.forEach { g.addVertex(it) }
        g.addEdge(vertices[0], vertices[1], 100.0)
        g.addEdge(vertices[2], vertices[3])
        val communities = g.louvain()
        assertEquals(true, communities[vertices[0]] == communities[vertices[1]])
        assertEquals(true, communities[vertices[1]] != communities[vertices[2]])
        assertEquals(true, communities[vertices[2]] == communities[vertices[3]])
    }
}
