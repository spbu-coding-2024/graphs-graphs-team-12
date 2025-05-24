package view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import viewmodel.GraphVM


@Composable
fun graphView(viewModel: GraphVM,
              scope: DrawScope) {
    Box(modifier = Modifier
        .fillMaxSize()) {
        viewModel.v.values.forEach {
            v -> vertexView(v, scope)
        }
        viewModel.e.values.forEach {
            e -> edgeView(e, scope, viewModel.g.directed)
        }
    }
}
