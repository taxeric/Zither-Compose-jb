import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ext.toObj
import page.home.MainScreen
import page.setting.SettingScreen
import page.common.CustomTab
import page.common.SimpleRadioGroup
import page.sign.SignScreen
import utils.adb.AdbProcess
import utils.FileUtil
import utils.adb.adbInfoFlow
import utils.sign.SignProcess
import utils.sign.signFlow

@Composable
@Preview
fun App() {
    var selectedIndex by remember {
        mutableStateOf(0)
    }
    val tabs = mutableListOf<CustomTab>().apply {
        add(CustomTab("常用"))
        add(CustomTab("签名"))
        add(CustomTab("设置"))
    }
    produceState(initialValue = Cache.default(), key1 = Unit) {
        localConfigPath = System.getProperty("user.dir")
        value = FileUtil.read(localConfigPath, localConfigFilename)
            .toObj()?: Cache.default()
        localCache = value
        AdbProcess.adbPath = value.adb.path
        adbInfoFlow.tryEmit(localCache.adb)
        SignProcess.zipalignPath = value.sign.zipalignPath
        SignProcess.apksignerPath = value.sign.apksignerPath
        signFlow.tryEmit(localCache.sign)
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
                when (selectedIndex) {
                    0 -> {
                        MainScreen()
                    }
                    1 -> {
                        SignScreen()
                    }
                    2 -> {
                        SettingScreen()
                    }
                }
            }
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Zither",
        resizable = false,
//        transparent = true,
        undecorated = true,
    ) {
        Column {
            WindowDraggableArea {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color.White)
                ) {
                    Text(
                        "Zither",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                    IconButton(
                        onClick = ::exitApplication,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                    ) {
                        Icon(Icons.Filled.Close, "")
                    }
                }
            }
            App()
        }
    }
}
