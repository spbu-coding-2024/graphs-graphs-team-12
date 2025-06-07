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
        Window(onCloseRequest = ::exitApplication, title = "") {
            app()
        }
    }
