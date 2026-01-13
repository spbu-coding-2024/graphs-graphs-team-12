package view.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import view.windows.fileKitOpener

enum class ButtonType {
    SQL,
    JSON,
    NEO4J,
}

@Composable
fun startUpButton(
    type: ButtonType,
    text: String,
    showGraph: MutableState<Boolean>,
    file: MutableState<String>,
) {
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
        when (type) {
            ButtonType.SQL -> fileKitOpener(openWin, "db", showGraph, file)
            ButtonType.JSON -> fileKitOpener(openWin, "json", showGraph, file)
            ButtonType.NEO4J -> fileKitOpener(openWin, "neo4j", showGraph, file)
        }
    }
}
