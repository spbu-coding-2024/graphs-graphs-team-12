package view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import viewmodel.EdgeVM
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun edgeView(
    viewModel: EdgeVM,
    directed: Boolean,
) {
    val density = LocalDensity.current.density
    val color: State<Color> = viewModel.color
    val x1 = viewModel.startPointX.value.toFloat()
    val y1 = viewModel.startPointY.value.toFloat()
    val x2 = viewModel.endPointX.value.toFloat()
    val y2 = viewModel.endPointY.value.toFloat()
    val startPixel = Offset(x1 * density, y1 * density)
    val endPixel = Offset(x2 * density, y2 * density)

    val showLabel = viewModel.showLabel
    val label = viewModel.label
    Canvas(
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        drawLine(
            color = color.value,
            start = startPixel,
            end = endPixel,
            strokeWidth = 2f,
        )
        if (directed) {
            val arrowSize = 20.0
            val arrowAngleDeg = 30.0

            val dx = endPixel.x - startPixel.x
            val dy = endPixel.y - startPixel.y
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

            val line1End = getArrowLine(endPixel, arrowAngleDeg)
            val line2End = getArrowLine(endPixel, -arrowAngleDeg)

            drawLine(
                color = color.value,
                start = endPixel,
                end = line1End,
                strokeWidth = 2f,
            )
            drawLine(
                color = color.value,
                start = endPixel,
                end = line2End,
                strokeWidth = 2f,
            )
        }
    }
    if (showLabel.value) {
        val scale = if (directed) 0.2f else 0.5f
        val labelX = x2 - (x2 - x1) * scale
        val labelY = y2 - (y2 - y1) * scale
        Text(
            text = label,
            modifier =
                Modifier
                    .offset(x = Dp(labelX), y = Dp(labelY + 0.1f)),
        )
    }
}
