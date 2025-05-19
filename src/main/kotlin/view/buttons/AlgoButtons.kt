package view.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun algoButton(algoName: String) {
    Button(
        onClick = {},
        modifier =
            Modifier
                .fillMaxWidth(),
    ) {
        Text(algoName)
    }
}
