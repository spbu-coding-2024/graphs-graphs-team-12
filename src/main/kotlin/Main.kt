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
        val sampleGraph = Graph(false, true)
        val firstV = Vertex(10.0, 20.0, "вершина 1")
        val secondV = Vertex(300.0, 10.0, "вершина 2")
        val thirdV = Vertex(10.0, 1500.0, "вершина 3")
        sampleGraph.addVertex(thirdV)
        sampleGraph.addVertex(firstV)
        sampleGraph.addVertex(secondV)
        sampleGraph.addEdge(firstV, secondV)
        sampleGraph.addEdge(firstV, thirdV)
        sampleGraph.addEdge(secondV, thirdV, 100.0)
        // sampleGraph.addEdge(secondV, firstV)
        val loader = GraphLoader(sampleGraph, "src/test/kotlin/modelTests/forDbFiles/showG.db")
        loader.loadGraph()
        Window(onCloseRequest = ::exitApplication, title = "") {
            app()
        }
    }
