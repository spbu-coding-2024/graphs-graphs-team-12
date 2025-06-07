package model.io.neo4jIO

import graph.Graph
import graph.Vertex
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase

class Neo4jRepository(
    private val uri: String,
    private val user: String,
    private val password: String,
) {
    fun writeNeo4j(graph: Graph) {
        val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
        val session = driver.session()
        session.executeWrite { tx ->
            tx.run("MATCH (n) DETACH DELETE n").consume()
        }

        session.executeWrite { tx ->
            tx
                .run(
                    "CREATE (:GraphInfo{directed:\$directed, weighted:\$weighted})",
                    mapOf(
                        "directed" to graph.directed,
                        "weighted" to graph.weighted,
                    ),
                ).consume()
        }

        for (vertex in graph.vertices) {
            session.executeWrite { tx ->
                tx
                    .run(
                        "CREATE (v:Vertex{name:\$name, x:\$x, y:\$y})",
                        mapOf(
                            "name" to vertex.name,
                            "x" to vertex.x,
                            "y" to vertex.y,
                        ),
                    ).consume()
            }
        }

        for (edges in graph.edges.values) {
            for (edge in edges) {
                session.executeWrite { tx ->
                    tx
                        .run(
                            "MATCH (from:Vertex{name:\$fromName}), (to:Vertex{name:\$toName})" +
                                "CREATE (from)-[:CONNECTED {weight:\$weight}]->(to)",
                            mapOf(
                                "fromName" to edge.from.name,
                                "toName" to edge.to.name,
                                "weight" to edge.weight,
                            ),
                        ).consume()
                }
            }
        }
        session.close()
        driver.close()
    }

    fun readNeo4j(): Graph {
        val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
        val session = driver.session()
        var directed = false
        var weighted = false
        session.executeRead { tx ->
            val graphInfo =
                tx.run(
                    "MATCH (g:GraphInfo) RETURN g.directed AS directed, g.weighted AS weighted",
                )
            graphInfo.forEach { info ->
                directed = info["directed"].asBoolean()
                weighted = info["weighted"].asBoolean()
            }
        }

        val graph = Graph(directed, weighted)
        session.executeRead { tx ->
            val vertexMap = mutableMapOf<String, Vertex>()
            val vertices =
                tx.run(
                    "MATCH (v:Vertex) RETURN v.name AS name, v.x AS x, v.y AS y",
                )
            vertices.forEach { vertex ->
                val v = Vertex(vertex.get("x").asDouble(), vertex.get("y").asDouble(), vertex.get("name").asString())
                graph.addVertex(v)
                vertexMap[v.name] = v
            }

            val edges =
                tx.run(
                    "MATCH (from:Vertex)-[e:CONNECTED]->(to:Vertex) RETURN from.name AS fromName, to.name AS toName, e.weight AS weight",
                )
            edges.forEach { edge ->
                val fromName = edge.get("fromName").asString()
                val toName = edge.get("toName").asString()

                val fromVertex = vertexMap[fromName]
                val toVertex = vertexMap[toName]
                if (fromVertex != null && toVertex != null) {
                    graph.addEdge(fromVertex, toVertex, edge.get("weight").asDouble())
                }
            }
        }
        session.close()
        driver.close()
        return graph
    }
}
