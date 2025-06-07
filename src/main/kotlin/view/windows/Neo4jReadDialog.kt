package view.windows

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun neo4jReadDialog(
    onConfirm: (uri: String, user: String, password: String) -> Unit,
    onDismiss: () -> Unit,
) {
    val uri = remember { mutableStateOf("") }
    val user = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Подключение к Neo4j") },
        text = {
            Column {
                OutlinedTextField(
                    value = uri.value,
                    onValueChange = { uri.value = it },
                    label = { Text("URI") },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = user.value,
                    onValueChange = { user.value = it },
                    label = { Text("Пользователь") },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Пароль") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(uri.value, user.value, password.value)
            }) {
                Text("Подключиться")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Отмена")
            }
        },
    )
}
