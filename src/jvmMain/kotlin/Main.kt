import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import ext.toObj
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import page.home.MainScreen
import page.setting.SettingScreen
import page.common.CustomTab
import page.common.SimpleRadioGroup
import page.sign.SignScreen
import utils.adb.AdbProcess
import utils.FileUtil
import utils.ProcessManager
import utils.sign.SignProcess

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
            Column(
                modifier = Modifier
                    .weight(3f)
            ) {
                topDevice()
                SimpleRadioGroup(
                    tabs = tabs,
                    orientation = Orientation.Vertical,
                    onSelected = { index, _ ->
                        selectedIndex = index
                    },
                    defaultSelected = selectedIndex,
                    modifier = Modifier
                        .fillMaxWidth()
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

@Composable
fun topDevice(
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    var refreshState by remember { mutableStateOf(0) }
    val curDevice = produceState(initialValue = Device.default(), key1 = refreshState) {
        val result = ProcessManager.adbHelper.screenDensity()
        if (result.exitCode == 0) {
            val deviceModel = ProcessManager.adbHelper.deviceModel()
            value = Device(deviceModel.stdout.trim().replace("\n", ""), valid = true)
            currentDevice = value
            deviceFlow.tryEmit(currentDevice)
        } else {
            value = Device.default()
        }
    }.value
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Text(
                "当前设备",
                fontSize = 20.sp,
            )
            Icon(
                Icons.Filled.Refresh, "",
                modifier = Modifier
                    .clickable {
                        refreshState++
                    }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                curDevice.model,
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
            )
            if (curDevice.valid) {
                Icon(
                    Icons.Filled.KeyboardArrowRight, "",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { showDialog = !showDialog }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))
    }
    if (showDialog) {
        deviceInfoDialog { showDialog = !showDialog }
    }
}

@Composable
fun deviceInfoDialog(
    onClose: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val device = deviceFlow.collectAsState(initial = Device.default()).value
    Dialog(
        state = rememberDialogState(position = WindowPosition(Alignment.Center)),
        onCloseRequest = { onClose() },
        title = device.model
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            deviceItem(
                "brand",
                device.brand,
            ) {
                coroutineScope.launch {
                    val result = ProcessManager.adbHelper.deviceBrand()
                    if (result.exitCode == 0) {
                        currentDevice = currentDevice.copy(brand = result.stdout.trim().replace("\n", ""))
                        deviceFlow.tryEmit(currentDevice)
                    }
                }
            }
            deviceItem(
                "sdkVersion",
                device.sdkVersion.toString(),
            ) {
                coroutineScope.launch {
                    val result = ProcessManager.adbHelper.deviceBuildSDK()
                    if (result.exitCode == 0) {
                        currentDevice = currentDevice.copy(sdkVersion = result.stdout
                            .trim()
                            .replace("\n", "")
                            .toInt()
                        )
                        deviceFlow.tryEmit(currentDevice)
                    }
                }
            }
            deviceItem(
                "releaseVersion",
                device.releaseVersion.toString(),
            ) {
                coroutineScope.launch {
                    val result = ProcessManager.adbHelper.deviceBuildRelease()
                    if (result.exitCode == 0) {
                        currentDevice = currentDevice.copy(releaseVersion = result.stdout
                            .trim()
                            .replace("\n", "")
                            .toInt()
                        )
                        deviceFlow.tryEmit(currentDevice)
                    }
                }
            }
        }
    }
}

@Composable
private fun deviceItem(
    title: String,
    info: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(title, modifier = Modifier.weight(1f))
        Text(info, modifier = Modifier.weight(1f))
        Icon(
            Icons.Filled.Refresh, "",
            modifier = Modifier
                .weight(1f)
                .clickable { onClick.invoke() }
        )
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
