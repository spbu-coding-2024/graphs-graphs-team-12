package model.algo

import graph.Graph
import graph.Vertex
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

fun applyForceAtlas2(
    graph: Graph,
    useStrongGravity: Boolean = false,
) {
    val engine = ForceAtlas2Engine(graph, useStrongGravity)
    engine.run(100)
    engine.applyToGraph()
}

private class LNode(
    val originalVertex: Vertex,
    var x: Double = 0.0,
    var y: Double = 0.0,
    var oldDx: Double = 0.0,
    var oldDy: Double = 0.0,
    var dx: Double = 0.0,
    var dy: Double = 0.0,
    var mass: Double = 0.0,
)

private class LEdge(
    val n1: LNode,
    val n2: LNode,
    val weight: Double,
)

private class ForceAtlas2Engine(
    graph: Graph,
    private val strongGravityMode: Boolean,
) {
    private val nodes: List<LNode>
    private val edges: List<LEdge>

    private fun getDegree(graph: Graph): MutableMap<Vertex, Int> {
        val allDegree = mutableMapOf<Vertex, Int>()
        if (graph.directed) {
            for (valuesTo in graph.edges.values) {
                for (vert in valuesTo) {
                    allDegree[vert.to] = (allDegree[vert.to] ?: 0) + 1
                }
            }
        }
        for (vert in graph.vertices) {
            allDegree[vert] = (allDegree[vert] ?: 0) + (graph.edges[vert]?.size ?: 0)
        }
        return allDegree
    }

    private val jitterTolerance: Double = 1.0
    private val scalingRatio: Double = 2000.0 // Сила отталкивания
    private val gravity: Double = 0.1 // Сила гравитации
    private var speed: Double = 1.0
    private var speedEfficiency: Double = 1.0

    init {
        val vertexToLNode = mutableMapOf<Vertex, LNode>()
        val tempNodes = mutableListOf<LNode>()
        val degrees = getDegree(graph)
        for (v in graph.vertices) {
            val degree = degrees[v]!!
            val mass = (degree + 1).toDouble()
            var startX = v.x
            var startY = v.y
            if (startX == 0.0 && startY == 0.0) {
                startX = Math.random() * 1000 - 500
                startY = Math.random() * 1000 - 500
            }
            val n = LNode(originalVertex = v, x = startX, y = startY, mass = mass)
            tempNodes.add(n)
            vertexToLNode[v] = n
        }
        nodes = tempNodes
        val tempEdges = mutableListOf<LEdge>()
        val allEdges = graph.edges.values.flatten()
        for (edges in allEdges) {
            val from = vertexToLNode[edges.from]!!
            val to = vertexToLNode[edges.to]!!
            val newEdge = LEdge(n1 = from, n2 = to, edges.weight)
            tempEdges.add(newEdge)
        }
        edges = tempEdges
    }

    private fun linRepulsion(
        n1: LNode,
        n2: LNode,
        coefficient: Double = 0.0,
    ) {
        val xDist: Double = n1.x - n2.x
        val yDist: Double = n1.y - n2.y
        val distance2: Double = xDist * xDist + yDist * yDist
        if (distance2 > 0.0) {
            val factor: Double = coefficient * n1.mass * n2.mass / distance2
            n1.dx += xDist * factor
            n1.dy += yDist * factor
            n2.dx -= xDist * factor
            n2.dy -= yDist * factor
        }
    }

    private fun linGravity(
        n: LNode,
        g: Double,
        coefficient: Double,
    ) {
        val xDist: Double = n.x
        val yDist: Double = n.y
        val distance = sqrt(xDist * xDist + yDist * yDist)

        if (distance > 0) {
            val factor: Double = coefficient * n.mass * g / distance
            n.dx -= xDist * factor
            n.dy -= yDist * factor
        }
    }

    private fun strongGravity(
        n: LNode,
        g: Double,
        coefficient: Double,
    ) {
        val xDist = n.x
        val yDist = n.y

        if (xDist != 0.0 && yDist != 0.0) {
            val factor = coefficient * n.mass * g
            n.dx -= xDist * factor
            n.dy -= yDist * factor
        }
    }

    private fun linAttraction(
        edge: LEdge,
        coefficient: Double = 0.0,
    ) {
        val n1 = edge.n1
        val n2 = edge.n2
        val e = edge.weight
        val xDist = n1.x - n2.x
        val yDist = n1.y - n2.y
        val factor = -coefficient * e
        n1.dx += xDist * factor
        n1.dy += yDist * factor
        n2.dx -= xDist * factor
        n2.dy -= yDist * factor
    }

    private fun applyRepulsion(
        nodes: List<LNode>,
        coefficient: Double,
    ) {
        for (i in 0 until nodes.size) {
            for (j in 0 until i) {
                linRepulsion(nodes[i], nodes[j], coefficient)
            }
        }
    }

    private fun applyGravity(
        nodes: List<LNode>,
        gravity: Double,
        scalingRatio: Double,
        useStrongGravity: Boolean = false,
    ) {
        if (!useStrongGravity) {
            for (i in 0 until nodes.size) {
                linGravity(nodes[i], gravity / scalingRatio, scalingRatio)
            }
        } else {
            for (i in 0 until nodes.size) {
                strongGravity(nodes[i], gravity / scalingRatio, scalingRatio)
            }
        }
    }

    private fun applyAttraction(
        nodes: List<LNode>,
        edges: List<LEdge>,
        coefficient: Double,
    ) {
        for (edge in edges) {
            linAttraction(edge, coefficient)
        }
    }

    fun run(iterations: Int) {
        repeat(iterations) {
            for (n in nodes) {
                n.oldDx = n.dx
                n.oldDy = n.dy
                n.dx = 0.0
                n.dy = 0.0
            }
            applyRepulsion(nodes, scalingRatio)
            applyGravity(nodes, gravity, scalingRatio, strongGravityMode)
            applyAttraction(nodes, edges, 1.0)
            var swinging: Double
            var totalSwinging: Double = 0.0
            var totalEffectiveTraction: Double = 0.0
            for (n in nodes) {
                swinging = sqrt((n.oldDx - n.dx) * (n.oldDx - n.dx) + (n.oldDy - n.dy) * (n.oldDy - n.dy))
                totalSwinging += n.mass * swinging
                totalEffectiveTraction += 0.5 * n.mass * sqrt((n.oldDx + n.dx) * (n.oldDx + n.dx) + (n.oldDy + n.dy) * (n.oldDy + n.dy))
            }
            val estimatedOptimalJitterTolerance: Double = 0.5 * sqrt(nodes.size.toDouble())
            val minJT: Double = sqrt(estimatedOptimalJitterTolerance)
            val maxJT: Double = 10.0
            var jt: Double =
                jitterTolerance *
                    max(
                        minJT,
                        min(
                            maxJT,
                            estimatedOptimalJitterTolerance *
                                totalEffectiveTraction / ((nodes.size.toDouble()) * (nodes.size.toDouble())),
                        ),
                    )
            val minSpeedEfficiency: Double = 0.05
            if (totalSwinging / totalEffectiveTraction > 2) {
                if (speedEfficiency > minSpeedEfficiency) {
                    speedEfficiency *= 0.5
                }
                jt = max(jt, jitterTolerance)
            }
            val targetSpeed: Double = jt * speedEfficiency * totalEffectiveTraction / totalSwinging
            if (totalSwinging > jt * totalEffectiveTraction) {
                if (speedEfficiency > minSpeedEfficiency) {
                    speedEfficiency *= 0.7
                }
            } else if (speed < 1000) {
                speedEfficiency *= 1.3
            }
            val maxRise: Double = 0.5
            speed += min(targetSpeed - speed, maxRise * speed)
            for (n in nodes) {
                swinging = n.mass * sqrt((n.oldDx - n.dx) * (n.oldDx - n.dx) + (n.oldDy - n.dy) * (n.oldDy - n.dy))
                val factor = speed / (1.0 + sqrt(speed * swinging))
                n.x = n.x + (n.dx * factor)
                n.y = n.y + (n.dy * factor)
            }
        }
    }

    fun applyToGraph() {
        for (n in nodes) {
            n.originalVertex.x = n.x
            n.originalVertex.y = n.y
        }
    }
}
