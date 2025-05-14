package view.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import view.window

@Composable
fun startUpButton(text: String) {
    val open_win = remember { mutableStateOf(false) }
    Button(onClick = { open_win.value = true }, modifier = Modifier
        .fillMaxWidth()) {
        Text(text)
    }
    if (open_win.value) {
        window(open_win)
    }
}
