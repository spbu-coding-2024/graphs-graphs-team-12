package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
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
    val startPoint: Pair<State<Double>, State<Double>> =
        Pair(
            from.xVM,
            from.yVM,
        )
    val endPoint: Pair<State<Double>, State<Double>> =
        Pair(
            to.xVM,
            to.yVM,
        )
}
