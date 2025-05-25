package view.layouts

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import view.buttons.ButtonType
import view.buttons.startUpButton
import view.buttons.startUpText
import view.windows.graphWindow

@Composable
fun startUpLayout() {
    val show = remember { mutableStateOf(true) }
    val showGraph = remember { mutableStateOf((false)) }
    val file = remember { mutableStateOf("") }
    graphWindow(showGraph, file)
    if (show.value) {
        BoxWithConstraints(
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            val desiredWidth = Dp((maxWidth.value * 0.5).toFloat())
            val xOffset = Dp((maxWidth.value * 0.25).toFloat())
            val yOffset = Dp((maxHeight.value * 0.33).toFloat())

            Column(
                modifier =
                    Modifier
                        .width(desiredWidth)
                        .offset(
                            x = xOffset,
                            y = yOffset,
                        ),
            ) {
                startUpText("Выберите, каким образом хотите загрузить граф:")
                startUpButton(ButtonType.SQL, "SQLite database", show, showGraph, file)
                startUpButton(ButtonType.NEO4J, "Neo4j database", show, showGraph, file)
                startUpButton(ButtonType.JSON, "JSON", show, showGraph, file)
            }
        }
    }
}
