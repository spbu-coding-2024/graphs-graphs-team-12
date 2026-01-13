package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import graph.Edge

class EdgeVM(
    e: Edge,
    from: VertexVM,
    to: VertexVM,
    val directed: Boolean,
) {
    var color: MutableState<Color> = mutableStateOf(Color.Black)
    val showLabel = mutableStateOf(false)
    val label = e.weight.toString()
    val startPointX: State<Double> = derivedStateOf { from.xVM.value + from.radius.value / 2.0 }
    val startPointY: State<Double> = derivedStateOf { from.yVM.value + from.radius.value / 2.0 }

    val endPointX: State<Double> = derivedStateOf { to.xVM.value + to.radius.value / 2.0 }
    val endPointY: State<Double> = derivedStateOf { to.yVM.value + to.radius.value / 2.0 }
}
