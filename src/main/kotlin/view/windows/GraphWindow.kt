package view.windows

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import view.buttons.algoButton

@Composable
fun graphWindow(show: MutableState<Boolean>) {
    if (show.value) {
        println("Yoooo")
        BoxWithConstraints(
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            Column(
                modifier =
                    Modifier
                        .width(Dp((maxWidth.value * 0.2).toFloat())),
            ) {
                algoButton("Раскладка графа")
                algoButton("Выделить сообщества")
                algoButton("Выделить ключевые вершины")
            }
            Canvas(
                modifier =
                    Modifier
                        .size(Dp((maxWidth.value * 0.8).toFloat()), maxHeight)
                        .offset(x = Dp((maxWidth.value * 0.2).toFloat()))
                        .background(Color.LightGray),
            ) {
                val myScope = this
            }
        }
    }
}
