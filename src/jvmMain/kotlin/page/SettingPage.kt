package page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("setting")
    }
}
