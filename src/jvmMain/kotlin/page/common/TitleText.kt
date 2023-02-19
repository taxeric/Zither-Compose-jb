package page.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun titleText(
    title: String,
    fontSize: TextUnit = 20.sp,
    lineColor: Color = Color.Black,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            title,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = lineColor)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun subtitleText(
    title: String,
    lineColor: Color = Color.Gray,
    modifier: Modifier = Modifier
) {
    titleText(title, 16.sp, lineColor, modifier)
}
