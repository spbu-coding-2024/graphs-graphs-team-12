package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

class EdgeVM(val from: VertexVM, val to: VertexVM, val scope: DrawScope) {
    val color: MutableState<Color> = mutableStateOf(Color.Black)
    val startPoint: Pair<MutableState<Double>, MutableState<Double>> = Pair(from.xVM,
        from.yVM)
    val endPoint: Pair<MutableState<Double>, MutableState<Double>> = Pair(from.xVM,
        from.yVM)

}
