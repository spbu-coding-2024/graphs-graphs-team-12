package view.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import viewmodel.GraphVM
import viewmodel.GwButtonType

@Composable
fun algoButton(
    algoName: String,
    type: GwButtonType,
    viewModel: GraphVM?,
    height: Dp,
) {
    Button(
        onClick = {
            if (type == GwButtonType.SQLITELOAD || type == GwButtonType.JSON_SAVE) {
                viewModel?.pendingAction = type
                viewModel?.askInput?.value = true
            } else if (type == GwButtonType.NEO4JLOAD) {
                viewModel?.neo4jOpen?.value = true
            } else {
                viewModel?.execute(type)
            }
        },
        modifier =
            Modifier
                .fillMaxWidth()
                .height(height),
    ) {
        Text(text = algoName, textAlign = TextAlign.Center, fontSize = 11.sp)
    }
}
