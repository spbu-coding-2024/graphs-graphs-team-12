package model.graph

class Edge(
    internal val from: Vertex,
    internal val to: Vertex,
    internal val weight: Double = 1.0,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Edge) return false

        return this.from == other.from && this.to == other.to && this.weight == other.weight
    }

    override fun hashCode(): Int = this.from.hashCode() xor this.to.hashCode() xor this.weight.hashCode()
}
