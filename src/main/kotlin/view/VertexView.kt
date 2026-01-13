package view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import viewmodel.GraphVM
import viewmodel.VertexVM

@Composable
fun vertexView(
    viewModel: VertexVM,
    graphViewModel: GraphVM,
) {
    val x: State<Double> = viewModel.xVM
    val y: State<Double> = viewModel.yVM
    val radius: State<Double> = viewModel.radius
    val color: State<Color> = viewModel.color
    val showLabel: State<Boolean> = viewModel.showLabel
    val label: String = viewModel.label
    Box(
        modifier =
            Modifier
                .size(
                    Dp(radius.value.toFloat() * 2),
                    Dp(radius.value.toFloat() * 2),
                ).offset(
                    Dp(x.value.toFloat() - radius.value.toFloat()),
                    Dp(y.value.toFloat() - radius.value.toFloat()),
                ).background(
                    color = color.value,
                    shape = CircleShape,
                ).pointerInput(viewModel) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        viewModel.onDrag(dragAmount)
                    }
                }.clickable(
                    onClick = {
                        viewModel.selected.value = !viewModel.selected.value
                        if (viewModel.selected.value) {
                            viewModel.color.value = Color.Red
                        } else {
                            viewModel.color.value = Color.Black
                        }
                        if (viewModel.selected.value) {
                            graphViewModel.selected.add(viewModel)
                        } else {
                            graphViewModel.selected.remove(viewModel)
                        }
                    },
                ),
    ) {
    }
    if (showLabel.value) {
        Text(
            text = label,
            modifier =
                Modifier.offset(
                    x = Dp((x.value + radius.value).toFloat()),
                    y = Dp((y.value + radius.value).toFloat()),
                ),
        )
    }
//    scope.drawCircle(
//        color = viewModel.color.value,
//        radius = viewModel.radius.value.toFloat(),
//        center = Offset(viewModel.xVM.value.toFloat(), viewModel.yVM.value.toFloat()),
//        style = Fill,
//        alpha = 1.0f
//    )
}
