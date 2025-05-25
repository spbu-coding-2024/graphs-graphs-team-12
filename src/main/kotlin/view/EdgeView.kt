package view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import viewmodel.EdgeVM
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun edgeView(
    viewModel: EdgeVM,
    directed: Boolean,
) {
    val color: State<Color> = viewModel.color
    val startPoint: Pair<State<Double>, State<Double>> = viewModel.startPoint
    val endPoint: Pair<State<Double>, State<Double>> = viewModel.endPoint
    val start = Offset(startPoint.first.value.toFloat(), startPoint.second.value.toFloat())
    val end = Offset(endPoint.first.value.toFloat(), endPoint.second.value.toFloat())

    Canvas(
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        drawLine(
            color = color.value,
            start =
                Offset(
                    startPoint.first.value.toFloat(),
                    startPoint.second.value.toFloat(),
                ),
            end =
                Offset(
                    endPoint.first.value.toFloat(),
                    endPoint.second.value.toFloat(),
                ),
        )
        if (directed) {
            val arrowSize = 20.0
            val arrowAngleDeg = 30.0

            val dx = end.x - start.x
            val dy = end.y - start.y
            val mainAngle = kotlin.math.atan2(dy, dx)

            fun getArrowLine(
                endPoint: Offset,
                angleOffsetDeg: Double,
            ): Offset {
                val angle = mainAngle + Math.toRadians(angleOffsetDeg)
                return Offset(
                    x = (endPoint.x - arrowSize * cos(angle)).toFloat(),
                    y = (endPoint.y - arrowSize * sin(angle)).toFloat(),
                )
            }

            val line1End = getArrowLine(end, arrowAngleDeg)
            val line2End = getArrowLine(end, -arrowAngleDeg)

            drawLine(
                color = color.value,
                start = end,
                end = line1End,
                strokeWidth = 4f,
            )
            drawLine(
                color = color.value,
                start = end,
                end = line2End,
                strokeWidth = 4f,
            )
        }
    }
}
