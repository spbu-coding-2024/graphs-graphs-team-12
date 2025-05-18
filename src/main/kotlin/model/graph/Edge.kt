package model.graph

class Edge(
    internal val from: Vertex,
    internal val to: Vertex,
    internal val weight: Double = 1.0,
)
