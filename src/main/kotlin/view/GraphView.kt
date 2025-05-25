package view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import viewmodel.GraphVM

@Composable
fun graphView(viewModel: GraphVM) {
    Box(
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        viewModel.v.values.forEach { v ->
            vertexView(v)
        }
        viewModel.e.values.forEach { e ->
            edgeView(e, viewModel.graph.directed)
        }
    }
}
