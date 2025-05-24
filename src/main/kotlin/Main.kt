import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.vinceglb.filekit.FileKit
import view.layouts.startUpLayout

@Composable
fun app() {
    startUpLayout()
}


fun main() =
    application {
        FileKit.init("")

        Window(onCloseRequest = ::exitApplication, title = "") {
            app()
        }
    }