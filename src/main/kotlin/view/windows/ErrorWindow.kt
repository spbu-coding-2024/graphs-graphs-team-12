package view.windows

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.style.TextAlign

@Composable
fun errorWindow(
    show: MutableState<Boolean>,
    errorMessage: MutableState<String>,
) {
    if (show.value) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(
                    onClick = {
                        show.value = false
                        errorMessage.value = ""
                    },
                ) {
                    Text("Продолжить")
                }
            },
            text = { Text(text = errorMessage.value, textAlign = TextAlign.Center) },
        )
    }
}
