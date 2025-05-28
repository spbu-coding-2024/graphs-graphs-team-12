package view.windows

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import viewmodel.GraphVM
import viewmodel.GwButtonType

@Composable
fun loaderDialog(
    show: MutableState<Boolean>,
    loadFile: MutableState<String>,
    viewModel: MutableState<GraphVM?>,
) {
    if (show.value) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                Button(onClick = {
                    viewModel.value?.execute(GwButtonType.SQLITELOAD)
                    loadFile.value = ""
                    show.value = false
                }) { Text("Записать") }
            },
            dismissButton = {
                Button(
                    onClick = { show.value = false },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White),
                ) { Text("Отменить") }
            },
            text = {
                OutlinedTextField(
                    value = loadFile.value,
                    onValueChange = { newFile: String -> loadFile.value = newFile },
                    label = { Text("Файл для записи") },
                )
            },
        )
    }
}
