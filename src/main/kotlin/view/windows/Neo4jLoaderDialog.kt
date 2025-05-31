package view.windows

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import viewmodel.GraphVM
import viewmodel.GwButtonType

@Composable
fun neo4jLoaderDialog(viewModel: MutableState<GraphVM?>) {
    val graphViewModel = viewModel.value ?: return

    if (graphViewModel.neo4jOpen.value) {
        AlertDialog(
            onDismissRequest = { graphViewModel.neo4jOpen.value = false },
            title = { Text("Подключение к neo4j") },
            text = {
                Column {
                    OutlinedTextField(
                        value = graphViewModel.neo4jUri.value,
                        onValueChange = { graphViewModel.neo4jUri.value = it },
                        label = { Text("URI") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = graphViewModel.neo4jUser.value,
                        onValueChange = { graphViewModel.neo4jUser.value = it },
                        label = { Text("Пользователь") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = graphViewModel.neo4jPassword.value,
                        onValueChange = { graphViewModel.neo4jPassword.value = it },
                        label = { Text("Пароль") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    graphViewModel.neo4jOpen.value = false
                    graphViewModel.execute(GwButtonType.NEO4JLOAD)
                }) {
                    Text("Записать")
                }
            },
            dismissButton = {
                Button(onClick = {
                    graphViewModel.neo4jOpen.value = false
                }) {
                    Text("Отменить")
                }
            },
        )
    }
}
