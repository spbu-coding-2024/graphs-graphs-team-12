import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import graph.Graph
import graph.Vertex
import model.io.sqliteIO.GraphLoader
import view.layouts.startUpLayout

@Composable
fun app() {
    startUpLayout()
}

fun main() =
    application {
        val dirW = Graph(true, true)
        val vertices = ArrayList<Vertex>()
        repeat(4) {
            vertices.add(Vertex(100.0 + it.toDouble() * 50.0, 100.0 + it.toDouble() * 50.0, "вершина $it"))
        }
        repeat(4) { dirW.addVertex(vertices[it]) }
        dirW.addEdge(vertices[0], vertices[1], 3.0)
        dirW.addEdge(vertices[2], vertices[3], 5.0)
        dirW.addEdge(vertices[3], vertices[2], 4.0)

        val loader = GraphLoader(dirW, "src/test/kotlin/modelTests/forDbFiles/dirW.db")
        loader.loadGraph()
        Window(onCloseRequest = ::exitApplication, title = "") {
            app()
        }
    }
