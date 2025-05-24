package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import graph.Vertex

class VertexVM(val vertex: Vertex) {
    val xVM: MutableState<Double> = mutableDoubleStateOf(vertex.x)
    val yVM: MutableState<Double> = mutableDoubleStateOf(vertex.y)
    val radius: MutableState<Double> = mutableDoubleStateOf(1.0)
    val color: MutableState<Color> = mutableStateOf<Color>(Color.Blue)

    fun onDrag(offset: Offset) {
        xVM.value += offset.x
        yVM.value += offset.y
        vertex.x = xVM.value
        vertex.y = yVM.value
    }
}
