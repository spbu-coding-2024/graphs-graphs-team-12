package view.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

@Composable
fun startUpText(text: String) {
    Column( modifier = Modifier
    .fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(
            text,
            textAlign = TextAlign.Center
        )
    }
}