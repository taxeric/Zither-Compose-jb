import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ext.toObj
import page.MainScreen
import page.SettingScreen
import page.common.CustomTab
import page.common.SimpleRadioGroup
import utils.AdbInfo
import utils.AdbUtil
import utils.FileUtil
import utils.adbInfoFlow

@Composable
@Preview
fun App() {
    var selectedIndex by remember {
        mutableStateOf(0)
    }
    val tabs = mutableListOf<CustomTab>().apply {
        add(CustomTab("TAB1"))
        add(CustomTab("TAB2"))
    }
    produceState(initialValue = "", key1 = Unit) {
        localConfigPath = System.getProperty("user.dir")
        value = FileUtil.read(localConfigPath, localConfigFilename)
            .toObj<Cache>()?.adb?.path?: ""
        if (value.isNotEmpty()) {
            AdbUtil.adbPath = value
            adbInfoFlow.tryEmit(AdbInfo(path = value))
        }
    }.value
    MaterialTheme {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SimpleRadioGroup(
                tabs = tabs,
                orientation = Orientation.Vertical,
                onSelected = { index, _ ->
                    selectedIndex = index
                },
                defaultSelected = selectedIndex,
                modifier = Modifier
                    .weight(3f)
                    .padding(8.dp, 0.dp),
                contentModifier = Modifier
                    .padding(24.dp, 8.dp)
            ) {  tab, selected, childModifier ->
                Text(
                    text = tab.text,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    modifier = childModifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            if (selected == tab.tag) Color.DarkGray else Color.Transparent,
                            RoundedCornerShape(4.dp)
                        )
                        .padding(0.dp, 12.dp)
                )
            }
            Box(
                modifier = Modifier
                    .weight(7f)
            ) {
                if (selectedIndex == 0) {
                    MainScreen()
                } else if (selectedIndex == 1) {
                    SettingScreen()
                }
            }
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Zither"
    ) {
        App()
    }
}
