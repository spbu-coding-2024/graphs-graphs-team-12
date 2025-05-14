package view.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun startUpButton(text: String) {
    val openWin = remember { mutableStateOf(false) }
    Button(
        onClick = { openWin.value = true },
        modifier =
            Modifier
                .fillMaxWidth(),
    ) {
        Text(text)
    }
    if (openWin.value) {
    }
}
