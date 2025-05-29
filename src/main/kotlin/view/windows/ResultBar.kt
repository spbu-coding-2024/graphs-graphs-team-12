package view.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp

@Composable
fun resultBar(
    show: MutableState<Boolean>,
    message: MutableState<String>,
    result: MutableState<Double>,
    width: Dp,
    height: Dp,
) {
    if (show.value) {
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .offset(x = width * 0.83f, y = height * 0.9f)
                    .width(width * 0.15f)
                    .height(height * 0.07f)
                    .clickable(onClick = { show.value = false })
                    .background(
                        Color.White,
                        RoundedCornerShape(8f),
                    ),
        ) {
            Text(
                text = message.value + " ${result.value}",
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
            )
        }
    }
}
