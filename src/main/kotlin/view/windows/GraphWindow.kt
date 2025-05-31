package view.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
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
import view.buttons.switchButton
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
            val extension = file.value.split(".").last()
            val showErrorDialog = mutableStateOf(false)
            val errorMessage = mutableStateOf("")
            val askLoading = mutableStateOf(false)
            val loadTo = mutableStateOf("")
            val showResult = mutableStateOf(false)
            val resultMessage = mutableStateOf("")
            val result = mutableStateOf(0.0)
            val gVM =
                remember {
                    GraphVM(
                        file.value,
                        extension,
                        showErrorDialog,
                        errorMessage,
                        askLoading,
                        loadTo,
                        showResult,
                        resultMessage,
                        result,
                    )
                }
            errorWindow(showErrorDialog, errorMessage)
            loaderDialog(askLoading, loadTo, graphStateContainer)
            graphStateContainer.value = gVM
            neo4jLoaderDialog(graphStateContainer)
            BoxWithConstraints(
                modifier =
                    Modifier
                        .fillMaxSize(),
            ) {
                val height = maxHeight
                BoxWithConstraints(
                    modifier =
                        Modifier
                            .width(Dp((maxWidth.value * 0.85).toFloat()))
                            .offset(x = Dp((0.15 * maxWidth.value).toFloat()))
                            .transformable(state = state)
                            .background(Color.LightGray),
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
                    verticalArrangement = Arrangement.spacedBy(Dp(3f)),
                ) {
                    algoButton(
                        "Выделить сообщества",
                        GwButtonType.COMMUNITIES,
                        graphStateContainer.value,
                        Dp((height.value * 0.05).toFloat()),
                    )
                    algoButton(
                        "Найти минимальный остов",
                        GwButtonType.MST,
                        graphStateContainer.value,
                        Dp((height.value * 0.05).toFloat()),
                    )
                    algoButton(
                        "Найти компоненты сильной связности",
                        GwButtonType.SCC,
                        graphStateContainer.value,
                        Dp((height.value * 0.05).toFloat()),
                    )
                    algoButton(
                        "Кратчайший путь(Дейкстра)",
                        GwButtonType.DIJKSTRA,
                        graphStateContainer.value,
                        Dp((height.value * 0.05).toFloat()),
                    )
                    algoButton(
                        "Поиск циклов",
                        GwButtonType.CYCLES,
                        graphStateContainer.value,
                        Dp((height.value * 0.05).toFloat()),
                    )
                    algoButton(
                        "Выделение ключевых вершин",
                        GwButtonType.HARMONIC,
                        graphStateContainer.value,
                        Dp((height.value * 0.05).toFloat()),
                    )
                    algoButton(
                        "Сбросить все",
                        GwButtonType.CLEAN,
                        graphStateContainer.value,
                        Dp((height.value * 0.05).toFloat()),
                    )
                    algoButton(
                        "Загрузить граф в SQLite базу",
                        GwButtonType.SQLITELOAD,
                        graphStateContainer.value,
                        Dp((height.value * 0.05).toFloat()),
                    )
                    algoButton(
                        "Загрузить граф в Neo4j базу",
                        GwButtonType.NEO4JLOAD,
                        graphStateContainer.value,
                        Dp((height.value * 0.05).toFloat()),
                    )
                    switchButton(
                        GwButtonType.EDGELABELS,
                        "Подписать веса ребер",
                        graphStateContainer,
                        Dp((height.value * 0.05).toFloat()),
                    )
                    switchButton(
                        GwButtonType.VERTICESLABELS,
                        "Подписать имена вершин",
                        graphStateContainer,
                        Dp((height.value * 0.05).toFloat()),
                    )
                }
                resultBar(showResult, resultMessage, result, maxWidth, maxHeight)
            }
        }
    }
}
