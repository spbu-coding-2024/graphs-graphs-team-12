package view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import viewmodel.VertexVM

@Composable
fun vertexView(viewModel: VertexVM, scope: DrawScope) {
    val x: State<Double> = viewModel.xVM
    val y: State<Double> = viewModel.yVM
    val radius: State<Double> = viewModel.radius
    val color: State<Color> = viewModel.color

    Box(modifier = Modifier
        .size(Dp(radius.value.toFloat() * 2),
            Dp(radius.value.toFloat() * 2))
        .offset(Dp(x.value.toFloat()), Dp(y.value.toFloat()))
        .background(
            color = color.value,
            shape = CircleShape
        )
        .pointerInput(viewModel) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                viewModel.onDrag(dragAmount)
        }
        }) {
    }
//    scope.drawCircle(
//        color = viewModel.color.value,
//        radius = viewModel.radius.value.toFloat(),
//        center = Offset(viewModel.xVM.value.toFloat(), viewModel.yVM.value.toFloat()),
//        style = Fill,
//        alpha = 1.0f
//    )
}
