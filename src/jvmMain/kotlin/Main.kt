import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.launch
import utils.AdbHelper

@Composable
@Preview
fun App() {

    var tips by remember { mutableStateOf("") }
    val coroutine = rememberCoroutineScope()
    MaterialTheme {
        Column {
            Button(onClick = {
                coroutine.launch {
                    val result = AdbHelper.version()
                    tips = result.stdout + "\n" + result.stderr
                }
            }) {
                Text("test")
            }
            Text(tips)
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
