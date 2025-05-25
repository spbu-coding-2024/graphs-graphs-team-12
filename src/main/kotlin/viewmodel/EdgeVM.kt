package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

class EdgeVM(
    from: VertexVM,
    to: VertexVM,
    val directed: Boolean,
) {
    val color: MutableState<Color> = mutableStateOf(Color.Black)
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
