package view.layouts

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import view.buttons.startUpText
import view.buttons.startUpButton

@Composable
fun startUpLayout() {
    BoxWithConstraints( modifier = Modifier
        .fillMaxSize()) {
        val desiredWidth = Dp((maxWidth.value * 0.5).toFloat())
        val xOffset = Dp((maxWidth.value * 0.25).toFloat())
        val yOffset = Dp((maxHeight.value * 0.33).toFloat())

        Column( modifier = Modifier
            .width(desiredWidth)
            .offset(x = xOffset,
                    y = yOffset)
        )
        {
            startUpText("Выберите, каким образом хотите загрузить граф:")
            startUpButton("SQLite database")
            startUpButton("Neo4j databse")
            startUpButton("JSON")
        }
    }
}
