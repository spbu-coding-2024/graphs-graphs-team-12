package view.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import viewmodel.GraphVM
import viewmodel.GwButtonType

@Composable
fun switchButton(
    switchType: GwButtonType,
    switchText: String,
    viewModel: MutableState<GraphVM?>,
    height: Dp,
) {
    var checked by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(height)
                .background(Color.White),
    ) {
        Text(
            text = switchText,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W500,
            modifier =
                Modifier
                    .padding(bottom = Dp(5f)),
        )
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                viewModel.value?.execute(switchType)
            },
            modifier =
                Modifier
                    .scale(1.2f),
        )
    }
}
