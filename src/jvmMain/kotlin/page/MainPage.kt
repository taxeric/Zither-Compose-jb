package page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen() {
    val coroutine = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp, 0.dp)
            ) {
                Text("安装APK")
            }
            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp, 0.dp)
            ) {
                Text("卸载APK")
            }
            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp, 0.dp)
            ) {
                Text("清除数据")
            }
        }
    }
}
