package view.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import view.buttons.algoButton
import view.graphView
import viewmodel.GraphVM
import viewmodel.GwButtonType

@Composable
fun graphWindow(
    show: MutableState<Boolean>,
    file: MutableState<String>,
) {
    if (show.value) {
        val rrr: MutableState<GraphVM?> = mutableStateOf(null)
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
                algoButton("mst", GwButtonType.MST, rrr.value)
                algoButton("Выделить сообщества", GwButtonType.COMMUNITIES, rrr.value)
                algoButton("Выделить ключевые вершины", GwButtonType.SCC, rrr.value)
            }
            BoxWithConstraints(
                modifier =
                    Modifier
                        .size(Dp((maxWidth.value * 0.8).toFloat()), maxHeight)
                        .offset(x = Dp((maxWidth.value * 0.2).toFloat()))
                        .clipToBounds(),
            ) {
                var scale by remember { mutableStateOf(1f) }
                var offset by remember { mutableStateOf(Offset.Zero) }
                val state =
                    rememberTransformableState { zoomChange, offsetChange, _ ->
                        scale *= zoomChange
                        offset += offsetChange
                    }
                BoxWithConstraints(
                    modifier =
                        Modifier
                            .background(Color.LightGray)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                translationX = offset.x
                                translationY = offset.y
                            }.transformable(state = state),
                ) {
                    val gVM =
                        remember {
                            GraphVM(
                                file.value,
                                mutableStateOf<Float>(maxWidth.value),
                                mutableStateOf<Float>(maxHeight.value),
                            )
                        }
                    rrr.value = gVM
                    graphView(gVM)
                }
            }
        }
    }
}
