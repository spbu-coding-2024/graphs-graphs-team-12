package view.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            val extension: String
            if (file.value.startsWith("bolt") || file.value.startsWith("neo4j")) {
                extension = "neo4j"
            } else {
                extension = file.value.split(".").last()
            }
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
                            .background(Color.White)
                            .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    val btnHeight = 45.dp
                    algoButton(
                        "Выделить сообщества",
                        GwButtonType.COMMUNITIES,
                        graphStateContainer.value,
                        btnHeight,
                    )
                    algoButton(
                        "Раскладка (ForceAtlas2)",
                        GwButtonType.LAYOUT,
                        graphStateContainer.value,
                        btnHeight,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        val isChecked = graphStateContainer.value?.isStrongGravity?.value ?: false
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = {
                                graphStateContainer.value?.isStrongGravity?.value = it
                            },
                        )
                        Text("Сильная гравитация", fontSize = 12.sp)
                    }
                    algoButton(
                        "Найти минимальный остов",
                        GwButtonType.MST,
                        graphStateContainer.value,
                        btnHeight,
                    )
                    algoButton(
                        "Найти компоненты сильной связности",
                        GwButtonType.SCC,
                        graphStateContainer.value,
                        btnHeight,
                    )
                    algoButton(
                        "Дейкстра",
                        GwButtonType.DIJKSTRA,
                        graphStateContainer.value,
                        btnHeight,
                    )
                    algoButton(
                        "Форд-Беллман",
                        GwButtonType.BELLMAN_FORD,
                        graphStateContainer.value,
                        btnHeight,
                    )
                    algoButton(
                        "Поиск циклов",
                        GwButtonType.CYCLES,
                        graphStateContainer.value,
                        btnHeight,
                    )
                    algoButton(
                        "Найти мосты",
                        GwButtonType.BRIDGES,
                        graphStateContainer.value,
                        btnHeight,
                    )
                    algoButton(
                        "Выделение ключевых вершин",
                        GwButtonType.HARMONIC,
                        graphStateContainer.value,
                        btnHeight,
                    )
                    algoButton(
                        "Сбросить все",
                        GwButtonType.CLEAN,
                        graphStateContainer.value,
                        btnHeight,
                    )
                    algoButton(
                        "Загрузить граф в SQLite базу",
                        GwButtonType.SQLITELOAD,
                        graphStateContainer.value,
                        btnHeight,
                    )
                    algoButton(
                        "Загрузить граф в Neo4j базу",
                        GwButtonType.NEO4JLOAD,
                        graphStateContainer.value,
                        btnHeight,
                    )
                    algoButton(
                        "Сохранить JSON",
                        GwButtonType.JSON_SAVE,
                        graphStateContainer.value,
                        btnHeight,
                    )
                    switchButton(
                        GwButtonType.EDGELABELS,
                        "Подписать веса ребер",
                        graphStateContainer,
                        btnHeight,
                    )
                    switchButton(
                        GwButtonType.VERTICESLABELS,
                        "Подписать имена вершин",
                        graphStateContainer,
                        btnHeight,
                    )
                }
                resultBar(showResult, resultMessage, result, maxWidth, maxHeight)
            }
        }
    }
}
