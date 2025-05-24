package view.layouts

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import view.buttons.ButtonType
import view.buttons.startUpButton
import view.buttons.startUpText

@Composable
fun startUpLayout() {
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
            startUpButton(ButtonType.SQL, "SQLite database")
            startUpButton(ButtonType.NEO4J, "Neo4j database")
            startUpButton(ButtonType.JSON, "JSON")
        }
    }
}
