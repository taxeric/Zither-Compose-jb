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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun titleText(
    title: String,
    lineColor: Color = Color.Black,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = lineColor)
    }
}
