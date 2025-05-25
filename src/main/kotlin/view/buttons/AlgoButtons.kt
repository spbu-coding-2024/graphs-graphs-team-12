package view.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import viewmodel.GraphVM
import viewmodel.GwButtonType

@Composable
fun algoButton(
    algoName: String,
    type: GwButtonType,
    viewModel: GraphVM?,
) {
    Button(
        onClick = { viewModel?.execute(type) },
        modifier =
            Modifier
                .fillMaxWidth(),
    ) {
        Text(algoName)
    }
}
