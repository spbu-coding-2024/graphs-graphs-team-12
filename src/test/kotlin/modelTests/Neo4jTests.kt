package modelTests

import graph.Graph
import graph.Vertex
import model.io.neo4jIO.Neo4jRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.neo4j.harness.Neo4j
import org.neo4j.harness.Neo4jBuilders
import kotlin.test.assertEquals

class Neo4jTests {
    private lateinit var g: Graph
    private lateinit var neo4j: Neo4j
    private lateinit var repo: Neo4jRepository

    @BeforeEach
    fun setup() {
        neo4j = Neo4jBuilders.newInProcessBuilder().withDisabledServer().build()
        repo = Neo4jRepository(neo4j.boltURI().toString(), "user", "password")
    }

    @AfterEach
    fun close() {
        neo4j.close()
    }

    @DisplayName("Simple graph without edges")
    @Test
    fun isoVertices() {
        g = Graph(false, false)
        val firstVertex = Vertex(1.0, 1.0, "A")
        val secondVertex = Vertex(1.0, 1.0, "B")
        g.addVertex(firstVertex)
        g.addVertex(secondVertex)

        repo.writeNeo4j(g)
        val loadedG = repo.readNeo4j()

        assertEquals(loadedG.directed, g.directed)
        assertEquals(loadedG.weighted, g.weighted)
        assertEquals(loadedG.vertices.size, g.vertices.size)
        assertEquals(loadedG.edges, g.edges)
        val names = loadedG.vertices.map { it.name }
        assertEquals(true, names.containsAll(listOf("A", "B")))
    }

    @DisplayName("Simple graph with some edges as well")
    @Test
    fun simpleGraph() {
        g = Graph(true, true)
        val firstVertex = Vertex(1.0, 1.0, "A")
        val secondVertex = Vertex(1.0, 1.0, "B")
        val thirdVertex = Vertex(1.0, 1.0, "C")
        g.addVertex(firstVertex)
        g.addVertex(secondVertex)
        g.addVertex(thirdVertex)
        g.addEdge(firstVertex, secondVertex, 10.0)
        g.addEdge(firstVertex, thirdVertex, 20.0)
        g.addEdge(thirdVertex, firstVertex, 5.0)

        repo.writeNeo4j(g)
        val loadedG = repo.readNeo4j()

        assertEquals(g.vertices.size, loadedG.vertices.size)
        val names = loadedG.vertices.map { it.name }
        assertEquals(true, names.containsAll(listOf("A", "B", "C")))

        val loadedEdges = loadedG.edges.flatMap { it.value }
        assertEquals(3, loadedEdges.size)
        assertEquals(true, loadedEdges.any { it.weight == 20.0 })
        val firstEdge = loadedEdges.first { it.weight == 20.0 }
        assertEquals(true, loadedEdges.any { it.weight == 5.0 })
        val secondEdge = loadedEdges.first { it.weight == 5.0 }
        assertEquals(firstEdge.from, secondEdge.to)
        assertEquals(firstEdge.to, secondEdge.from)
        assertEquals("A", firstEdge.from.name)
        assertEquals("C", firstEdge.to.name)
    }
}
