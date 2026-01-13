package modelTests.algo

import graph.Graph
import graph.Vertex
import model.algo.applyForceAtlas2
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.hypot
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ForceAtlas2Test {
    lateinit var g: Graph

    @BeforeEach
    fun setup() {
        g = Graph(directed = false, weighted = true)
    }

    @Test
    fun `algorithm runs on empty graph without errors`() {
        applyForceAtlas2(g)
        assertTrue(g.vertices.isEmpty())
    }

    @Test
    fun `algorithm runs on single vertex graph`() {
        val v = Vertex(0.0, 0.0)
        g.addVertex(v)
        applyForceAtlas2(g)
        assertNotEquals(Double.NaN, v.x)
        assertNotEquals(Double.NaN, v.y)
    }

    @Test
    fun `vertices move from zero coordinates`() {
        val v1 = Vertex(0.0, 0.0)
        val v2 = Vertex(0.0, 0.0)
        g.addVertex(v1)
        g.addVertex(v2)
        g.addEdge(v1, v2)
        applyForceAtlas2(g)
        val moved = (v1.x != 0.0 || v1.y != 0.0) && (v2.x != 0.0 || v2.y != 0.0)
        assertTrue(moved)
        val distance = hypot(v1.x - v2.x, v1.y - v2.y)
        assertTrue(distance > 0.01)
    }

    @Test
    fun `disconnected nodes repel each other`() {
        val v1 = Vertex(0.0, 0.0)
        val v2 = Vertex(0.0, 0.0)
        g.addVertex(v1)
        g.addVertex(v2)
        applyForceAtlas2(g)
        val distance = hypot(v1.x - v2.x, v1.y - v2.y)
        assertTrue(distance > 0.01)
    }

    @Test
    fun `strong gravity mode runs without errors`() {
        val v1 = Vertex(10.0, 10.0)
        val v2 = Vertex(-10.0, -10.0)
        g.addVertex(v1)
        g.addVertex(v2)
        g.addEdge(v1, v2)
        applyForceAtlas2(g, useStrongGravity = true)
        assertFalse(v1.x.isInfinite())
        assertFalse(v1.y.isInfinite())
    }
}
