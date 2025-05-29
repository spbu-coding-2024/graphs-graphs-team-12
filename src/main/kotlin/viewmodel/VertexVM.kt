package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import graph.Vertex

class VertexVM(
    val vertex: Vertex,
) {
    val xRatio: MutableState<Double> = mutableDoubleStateOf(vertex.x)
    val yRatio: MutableState<Double> = mutableDoubleStateOf(vertex.y)
    val xVM: MutableState<Double> = mutableDoubleStateOf(xRatio.value)
    val yVM: MutableState<Double> = mutableDoubleStateOf(yRatio.value)
    val radius: MutableState<Double> = mutableDoubleStateOf(10.0)
    val color: MutableState<Color> = mutableStateOf<Color>(Color.Black)
    val showLabel = mutableStateOf(false)
    val label = vertex.name

    fun onDrag(offset: Offset) {
        xVM.value = xVM.value + offset.x
//        if (xVM.value > xMax.value) {
//            xVM.value = xMax.value.toDouble() - this.radius.value
//        }
//        if (xVM.value < 0) {
//            xVM.value = 0.0 + this.radius.value
//        }
        vertex.x = xVM.value

        yVM.value = yVM.value + offset.y
//        if (yVM.value > yMax.value) {
//            yVM.value = yMax.value.toDouble() - this.radius.value
//        }
//        if (yVM.value < 0) {
//            yVM.value = 0.0 + this.radius.value
//        }
        vertex.y = yVM.value
//        if (offset.x > 0) {
//            xVM.value =
//                (
//                    if (xVM.value + offset.x < xMax.value) {
//                        xVM.value + offset.x
//                    } else {
//                        xMax.value
//                    }
//                ).toDouble()
//        } else {
//            xVM.value =
//                (
//                    if (xVM.value + offset.x > 0.0) {
//                        xVM.value + offset.x
//                    } else {
//                        0.0
//                    }
//                ).toDouble()
//        }
//        if (offset.y > 0) {
//            yVM.value =
//                (
//                    if (yVM.value + offset.y < yMax.value) {
//                        yVM.value + offset.y
//                    } else {
//                        yMax.value
//                    }
//                ).toDouble()
//        } else {
//            yVM.value =
//                (
//                    if (yVM.value + offset.y > 0.0) {
//                        yVM.value + offset.y
//                    } else {
//                        0.0
//                    }
//                ).toDouble()
//        }
    }
}
