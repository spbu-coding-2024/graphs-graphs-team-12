package view.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Window
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
        Window(onCloseRequest = {
            file.value = ""
            show.value = false
        }, title = "") {
            val graphStateContainer: MutableState<GraphVM?> = mutableStateOf(null)
            var scale by remember { mutableStateOf(1f) }
            var offset by remember { mutableStateOf(Offset.Zero) }
            val state =
                rememberTransformableState { zoomChange, offsetChange, _ ->
                    scale *= zoomChange
                    offset += offsetChange
                }
            val gVM =
                remember {
                    GraphVM(
                        file.value,
                    )
                }
            graphStateContainer.value = gVM
            BoxWithConstraints(
                modifier =
                    Modifier
                        .fillMaxSize(),
            ) {
                BoxWithConstraints(
                    modifier =
                        Modifier
                            .width(Dp((maxWidth.value * 0.85).toFloat()))
                            .offset(x = Dp((0.15 * maxWidth.value).toFloat()))
                            .transformable(state = state)
                            .background(Color.LightGray),
//                            .combinedClickable(
//                                onClick = {},
//                                onDoubleClick = { scale *= 2f },
//                            ),
                ) {
                    BoxWithConstraints(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                    translationX = offset.x
                                    translationY = offset.y
                                }.transformable(state = state)
                                .background(Color.LightGray),
                    ) {
                        graphView(gVM)
                    }
                }
                Column(
                    modifier =
                        Modifier
                            .width(Dp((maxWidth.value * 0.15).toFloat()))
                            .height(maxHeight)
                            .background(Color.White),
                ) {
                    algoButton("mst", GwButtonType.MST, graphStateContainer.value)
                    algoButton("Выделить сообщества", GwButtonType.COMMUNITIES, graphStateContainer.value)
                    algoButton("Выделить ключевые вершины", GwButtonType.SCC, graphStateContainer.value)
                }
            }
        }
    }
}
