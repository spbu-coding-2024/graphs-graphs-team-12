import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
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
